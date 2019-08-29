#include <ESP8266WiFi.h>
#include <DNSServer.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h>         // https://github.com/tzapu/WiFiManager

#define HOST "soldier.cloudmqtt.com"
#define PORT 16925
#define HOSTUSER "inkphijn"
#define HOSTPASSWORD "F1QexKFsttXE"
#define PUBLISH_TOPIC "fiap/waterclock/iot/devtype/nodemcu/id/23/sensor"
#define TIMEOUT 5000 //milliseconds - conection timeout
#define REFRESH 1000 //milliseconds - refresh rate for the led and sensors data
#define ID 100

WiFiServer server(80);

WiFiClient espClient;
PubSubClient mqtt(espClient);

double calculoVazao;
volatile int pulso;
int contador = 0;
float fluxoAcumulado = 0;
float metroCubico = 0;
// PORTA DE ENTRADA
int interruption = 13;

void setup() {
  Serial.begin(115200);
  pinMode(interruption, INPUT);
  attachInterrupt(interruption, getVazao, RISING);
  WiFiManager wifiManager;

  //wifiManager.resetSettings();

  wifiManager.autoConnect("AutoConnectAP");
  Serial.println("Connected.");

  mqtt.setServer(HOST, PORT);
  mqtt.setCallback(callback);

  server.begin();
}

void loop() {
  // put your main code here, to run repeatedly:
  
  pulso = 0;
  interrupts();
  delay(1000);
  noInterrupts();

  //TESTAR CALCULOS DE VAZÃO
  //calculoVazao = (pulso * 2.25);
  calculoVazao = (pulso / 5.5); // CONVERTER para L/min
  fluxoAcumulado = fluxoAcumulado + calculoVazao;
  contador++;
  Serial.println(contador);
  Serial.println(fluxoAcumulado);
  if(contador%60 == 0){
    
    // make a String to hold incoming data from the client
    enviarMqttResponse();
  }
      // Close the connection 
  
    
}


void enviarMqttResponse(){
   if (WiFi.status() == WL_CONNECTED) { 
        reconn();
          //Checa mensagens - deve ser chamado de forma recorrente
          mqtt.loop();
          char texto[200];
          StaticJsonBuffer<200> jsonBuffer;
          JsonObject& json = jsonBuffer.createObject();
          json["id"] = ID;
          //String ip = String(WiFi.localIP())+" ";
          //json["ip"] = ip;
          if (!isnan(fluxoAcumulado)) {
            
            fluxoAcumulado = fluxoAcumulado/contador;
            json["fluxoAcumulado"] = fluxoAcumulado;
            contador = 0;
            fluxoAcumulado = 0;

          }
          //Criando a string a ser enviada via MQTT publish
          char buffer[256];
          json.printTo(buffer, sizeof(buffer));
          Serial.print("[MQTT] Publish...\n");
          mqtt.publish(PUBLISH_TOPIC, buffer);;
      }else {
        Serial.println("Não conectado ao AP");
        delay(REFRESH);
      }
}

//Função de callback que deve ser executada quando chega uma mensagem
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print(String("Message arrived [") + topic + "] ");
  payload[length] = '\0'; //com sorte há espaço para mais um caractere...
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& json = jsonBuffer.parseObject(payload);
  //PENSAR EM LÓGICA AQUI
  if (json.success() && json.containsKey("value")) {

  }
  delay(10);
}

//Função que verifica a conexão do cliente MQTT
void reconnect() {
  // Loop until we're reconnected
  while (!mqtt.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Attempt to connect
    if (mqtt.connect(clientId.c_str())) {
      Serial.println("MQTT client connected");
      // Once connected, resubscribe to LED topic
      // mqtt.subscribe(LED_TOPIC);
    } else {
      Serial.print("failed, rc=");
      Serial.print(mqtt.state());
      Serial.println(String(" try again in ") + TIMEOUT + " milliseconds");
      // Wait TIMEOUT seconds before retrying
      delay(TIMEOUT);
    }
  }
}

void getVazao() {
  pulso++;
}


void reconn(){
  if(!mqtt.connected()){
    Serial.print("Attempting MQTT connection...");
    if(mqtt.connect("ESP8266Client", HOSTUSER, HOSTPASSWORD)){
      Serial.println("Connected MQTT");
    }else {
      Serial.print("Failed, rc=");
      Serial.print(mqtt.state());
    }
  }

}
