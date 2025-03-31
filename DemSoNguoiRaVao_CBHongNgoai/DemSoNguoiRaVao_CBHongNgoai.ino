#include <SoftwareSerial.h>

#define ESP_TX 2
#define ESP_RX 3

const String WIFI_SSID = "3C Roastery Coffee";
const String WIFI_PASS = "caphedacsan";
String IP = "";

const int cbvao = 4;                       // Entrance sensor
const int cbra = 5;                        // Exit sensor
const int lightSensor = A0;                // Light sensor (LDR)
const int ledPins[] = { 6, 7, 8, 9, 10 };  // LEDs

int peopleCount = 0;
int lightThreshold = 110;

int lastTrigger = 0;
unsigned long lastTriggerTime = 0;
const int debounceTime = 500;
const int resetTime = 10000;

SoftwareSerial esp8266(ESP_TX, ESP_RX);

void setup() {
  pinMode(cbvao, INPUT_PULLUP);
  pinMode(cbra, INPUT_PULLUP);
  pinMode(lightSensor, INPUT);

  for (int i = 0; i < 6; i++) {
    pinMode(ledPins[i], OUTPUT);
  }

  Serial.begin(9600);
  esp8266.begin(9600);

  initWifiModule();  // Initialize Wi-Fi and ESP8266
}

void loop() {
  bool entranceSensor = digitalRead(cbvao) == LOW;
  bool exitSensor = digitalRead(cbra) == LOW;
  int lightLevel = analogRead(lightSensor);

  unsigned long currentTime = millis();

  if (!entranceSensor && !exitSensor && (currentTime - lastTriggerTime > resetTime)) {
    lastTrigger = 0;
  }

  if (entranceSensor && lastTrigger == 0 && (currentTime - lastTriggerTime > debounceTime)) {
    lastTrigger = 1;
    lastTriggerTime = currentTime;
  }
  if (exitSensor && lastTrigger == 1 && (currentTime - lastTriggerTime > debounceTime)) {
    peopleCount++;
    Serial.println("Person entered!");
    Serial.print("People in room: ");
    Serial.println(peopleCount);
    lastTrigger = 0;
    lastTriggerTime = currentTime;
    sendJSON();
  }

  if (exitSensor && lastTrigger == 0 && (currentTime - lastTriggerTime > debounceTime)) {
    lastTrigger = 2;
    lastTriggerTime = currentTime;
  }
  if (entranceSensor && lastTrigger == 2 && (currentTime - lastTriggerTime > debounceTime)) {
    if (peopleCount > 0) {
      peopleCount--;
      Serial.println("Person exited!");
    } else {
      Serial.println("No people left in the room!");
    }
    Serial.print("People in room: ");
    Serial.println(peopleCount);
    lastTrigger = 0;
    lastTriggerTime = currentTime;
    sendJSON();
  }

  bool isDark = (lightLevel < lightThreshold);
  int ledsToTurnOn = isDark ? max(peopleCount, 2) : 2;

  for (int i = 0; i < 6; i++) {
    digitalWrite(ledPins[i], (i < ledsToTurnOn) ? HIGH : LOW);
  }

  delay(50);
}

void sendJSON() {
  String host = "192.168.1.121";  
  int port = 8080;                 

  String json = "{\"count\":" + String(peopleCount) + "}";
  Serial.println(json);

  String cmd = "AT+CIPSTART=0,\"TCP\",\"" + host + "\"," + String(port) + "\r\n";
  sendCommand(cmd, 2000);

  String httpRequest = "POST /iot/data HTTP/1.1\r\n";
  httpRequest += "Host: " + host + ":" + String(port) + "\r\n";
  httpRequest += "Content-Type: application/json\r\n";  
  httpRequest += "Content-Length: " + String(json.length()) + "\r\n\r\n";
  httpRequest += json;  
  Serial.println(httpRequest);

  cmd = "AT+CIPSEND=0," + String(httpRequest.length()) + "\r\n";
  String sendResult = sendCommand(cmd, 2000);
  if (sendResult.indexOf(">") != -1) {
    esp8266.print(httpRequest);
    delay(1000);

    String response = "";
    long timeout = millis() + 5000;
    while (millis() < timeout) {
      while (esp8266.available()) {
        response += (char)esp8266.read();
      }
    }
    Serial.println("Server response: " + response);
  }

  sendCommand("AT+CIPCLOSE=0\r\n", 1000);
}

String sendCommand(String command, const int timeout) {
  Serial.print("Sending command: ");
  Serial.println(command);
  esp8266.print(command);

  String response = "";
  long int time = millis();

  while ((time + timeout) > millis()) {
    while (esp8266.available()) {
      char c = esp8266.read();
      response += c;
    }
  }

  Serial.println(response);
  return response;
}

String getIP(String response) {
  int start = response.indexOf("+CIFSR:STAIP,\"");
  if (start == -1) return "Not Found";

  start += 14;  
  int end = response.indexOf("\"", start);
  if (end == -1) return "Not Found";

  return response.substring(start, end);
}

void initWifiModule() {
  sendCommand("AT+RST\r\n", 2000);
  delay(1000);

  sendCommand("AT+CWMODE=1\r\n", 1500);
  delay(1000);

  sendCommand("AT+CWJAP=\"" + WIFI_SSID + "\",\"" + WIFI_PASS + "\"\r\n", 10000);
  delay(3000);

  String wifiIP = sendCommand("AT+CIFSR\r\n", 1500);
  IP = getIP(wifiIP);
  Serial.println("Device IP: " + IP);

  sendCommand("AT+CIPMUX=1\r\n", 1500);
  delay(1000);
}
