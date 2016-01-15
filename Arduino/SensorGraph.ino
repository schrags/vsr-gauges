void setup()
{
  Serial.begin(9600);
  pinMode(A0, INPUT);
  pinMode(A1, INPUT);
  pinMode(A2, INPUT);
}

void loop()
{
  String sensor1 = String(analogRead(A0));
  String sensor2 = String(analogRead(A1));
  String sensor3 = String(analogRead(A2));
  String sensor4 = String(analogRead(A3));
  Serial.print("{" + sensor1 + "|" + sensor2 + "|" + sensor3 + "|" + sensor4 + "}");
  delay(100);
}
