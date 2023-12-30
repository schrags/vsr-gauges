void setup()
{
  Serial.begin(9600);
}

void loop()
{
  String sensor1 = String(analogRead(A0));
  String sensor2 = String(analogRead(A1));
  String sensor3 = String(analogRead(A2));
  String sensor4 = String(analogRead(A3));
  String sensor5 = String(analogRead(A4));
  Serial.print("{" + sensor1 + "|" + sensor2 + "|" + sensor3 + "|" + sensor4 + "|" + sensor5 "}");
  delay(100);
}
