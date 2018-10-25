 // Incluimos las librerías necesarias
#include <TFT.h> //Cargamos la librería para pantalla
#include <Servo.h>//Incluimos la libreria servo


/**************************************************
 * Declaracion de valiables, especficacion de pines
 * ***********************************************/
//DEFINICION de variables para Servo1 y Servo2
Servo servo1;
Servo servo2;

///-----------------------DEFINICION de pines para controlar el motor---------------------------------
//MODULO Tb6612fng
//motor A conectado a A01 y A02
//motor B conectado a B01 y B02
//Motor A
int PWMA = 26; //Control de velocidad
int AIN2 = 28; //Direccion 2
int AIN1 = 30; //Direccion 1
int STBY = 32; //standby
//Motor B
int BIN1 = 34; //Direccion 1
int BIN2 = 36; //Direccion 2
int PWMB = 38; //Control de velocidad

//-----------------------------DEFINICION de pines para pantalla----------------------------------------
//MODULO Arduino TFT Screen
#define RESET  5   
#define DC_LD  6
#define CS_LD  7
//      MOSI   51
//      SCK    52

TFT TFTscreen = TFT(CS_LD, DC_LD, RESET);

//--------------------DEFINICION de pines para bluetooth (Arduino Mega)-----------------------------------
//MODULO HC-05
  //Modulo BT -> Arduino MEGA
  //RX        -> TX1=18
  //TX        -> RX1=19


void setup() {
  //Definimos los pines del servo 1 y servo 2
  servo1.attach(11);
  servo2.attach(10);
  //Posición inicial al Servo1 y Servo2
  servo1.write(90);
  servo2.write(90);

  //Definicion en modo salida para pines de motor -- MODULO Tb6612fng
  pinMode(STBY, OUTPUT);
  //Motor 1
  pinMode(PWMA, OUTPUT);
  pinMode(AIN1, OUTPUT);
  pinMode(AIN2, OUTPUT);
  //Motor2
  pinMode(PWMB, OUTPUT);
  pinMode(BIN1, OUTPUT);
  pinMode(BIN2, OUTPUT);
  
  //Se inicializa la librería para la pantalla
  TFTscreen.begin();
  // Limpia la pantalla y coloca el color que preferimos
  TFTscreen.background(192, 192, 192);
  //En caso de colocar texto en la pantalla se le atribuye el tamaño que se desea
  TFTscreen.setTextSize(2);
  TFTscreen.stroke(0, 0, 0);
  
  //Inicializamos serial para recibir y enviar los datos por bluetooth
  Serial.begin(9600);
  Serial1.begin(9600);
  //Enviamos un mensaje a la aplicación para verificar el estado de la conexión
  Serial1.print("Conexión Exitosa"); 
  Serial1.print("#"); 
  //Se muestra un Texto en la Pantalla
  TFTscreen.text("Robot Activado !", 20.0, 50);
}

void loop() {
  //Verifica que el Puerto Serial reciba Datos
  if (Serial1.available()) {
        char Dato; 
        Dato = Serial1.read(); 
        Serial.println(Dato);
  //Realiza una accion dependiendo de la letra que recibe
    if(Dato == 'a') 
        { 
        //Mueve el brazo1 hacia arriba
        delay(100); 
        Serial1.print("Arriba"); 
        Serial1.print("#"); 
        CentroArriba1();
        ArribaCentro1();
        }
    if(Dato == 'b') 
        { 
        //Mueve el brazo1 hacia abajo
        delay(100); 
        Serial1.print("Abajo"); 
        Serial1.print("#"); 
        CentroAbajo1();
        AbajoCentro1();
        }
    if(Dato == 'c') 
        { 
        //Mueve el brazo2 hacia arriba
        delay(100); 
        Serial1.print("Arriba"); 
        Serial1.print("#"); 
        CentroArriba2();
        ArribaCentro2();
        }
    if(Dato == 'd') 
        { 
        //Mueve el brazo2 hacia abajo
        delay(100); 
        Serial1.print("Abajo"); 
        Serial1.print("#"); 
        CentroAbajo2();
        AbajoCentro2();
        }

         
    if(Dato == 'e') 
        {
        //Robot se detiene 
        delay(100); 
        Detener();
        delay(500); 
        //Robot camina hacia adelante
        Serial1.print("Adelante"); 
        Serial1.print("#");  
        Caminar();
        }
    if(Dato == 'f') 
        {
        //Robot se detiene 
        delay(100); 
        Serial1.print("Detiene"); 
        Serial1.print("#"); 
        Detener();
        }
    if(Dato == 'g') 
        {
        //Robot se detiene 
        delay(100); 
        Detener();
        delay(500);
        //Robot Retrocede 
        Serial1.print("Retrocede"); 
        Serial1.print("#");  
        Retroceso();
        } 
    if(Dato == 'h') 
        {
        //Robot se detiene 
        delay(100); 
        Detener();
        delay(500); 
        //Robot gira a la izquierda
        Serial1.print("izquierda"); 
        Serial1.print("#");  
        GiraIzquierda();
        } 
    if(Dato == 'i') 
        {
        //Robot se detiene 
        delay(100); 
        Detener();
        delay(500); 
        //Robot gira a la derecha
        Serial1.print("derecha"); 
        Serial1.print("#");  
        GiraDerecha();
        }

         
     if(Dato == 'j') 
        {
        //En pantalla se carga un rostro con expresion Normal
        delay(100); 
        Serial1.print("Normal"); 
        Serial1.print("#");  
        Normal();
        }  

      if(Dato == 'k') 
        {
        //En pantalla se carga un rostro con expresión Alegre
        delay(100); 
        Serial1.print("Alegre"); 
        Serial1.print("#");  
        Alegre();
        }  
      if(Dato == 'l') 
        {
        //En pantalla se carga un rostro con expresión Triste
        delay(100); 
        Serial1.print("Triste"); 
        Serial1.print("#");  
        Triste();
        }  
 
  }delay(100);
}

    void CentroAbajo1(){
    //************************************
    //MOVIMIENTOS BRAZO IZQUIERDO [servo1]
    //************************************
      for (uint8_t i = 90; i <= 180; i++) {
      servo1.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º#");
      Serial.println();*/
      delay(5);   
      }
    }
     void AbajoCentro1(){
    //************************************
    //MOVIMIENTOS BRAZO IZQUIERDO [servo1]
    //************************************
      for (uint8_t i = 180; i >= 90; i--) {
      servo1.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º");
      Serial.println();*/
      delay(5);   
      }
     }

    void CentroArriba1(){
    //************************************
    //MOVIMIENTOS BRAZO IZQUIERDO [servo1]
    //************************************
      for (uint8_t i = 90; i >= 1; i--) {
      servo1.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º");
      Serial.println();*/
      delay(5); 
      }
    }
     void ArribaCentro1(){
    //************************************
    //MOVIMIENTOS BRAZO IZQUIERDO [servo1]
    //************************************
      for (uint8_t i = 1; i <= 90; i++) {
      servo1.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º");
      Serial.println();*/
      delay(5); 
      }
    }
    void CentroAbajo2(){
    //************************************
    //MOVIMIENTOS BRAZO DERECHO [servo2]
    //************************************
      for (uint8_t i = 90; i <= 180; i++) {
      servo2.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º");
      Serial.println();*/
      delay(5);   
      }
    }
     void AbajoCentro2(){
    //************************************
    //MOVIMIENTOS BRAZO DERECHO [servo2]
    //************************************
      for (uint8_t i = 180; i >= 90; i--) {
      servo2.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º");
      Serial.println();*/
      delay(5);   
      }
     }

    void CentroArriba2(){
    //************************************
    //MOVIMIENTOS BRAZO DERECHO [servo2]
    //************************************
      for (uint8_t i = 90; i >= 1; i--) {
      servo2.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º");
      Serial.println();*/
      delay(5); 
      }
    }
     void ArribaCentro2(){
    //************************************
    //MOVIMIENTOS BRAZO DERECHO [servo2]
    //************************************
      for (uint8_t i = 1; i <= 90; i++) {
      servo2.write(i);
      /*Serial.print("Dirección "); 
      Serial.print(i); 
      Serial.print(" º");
      Serial.println();*/
      delay(5); 
      }
    }

void mover(int motor, int velocidad, int direccion){
//Mueve el motor especificado a la velocidad y direccion requerida
//motor: 0 para el motor B y 1 para el motor A
//velocidad: 0 para detener, y 255 para la maxima velocidad
//direccion: 0 para ir en sentido de la agujas del reloj, 1 en sentido contrario

  digitalWrite(STBY, HIGH); //inhabilita standby
  
  boolean inPin1 = LOW;
  boolean inPin2 = HIGH;
  
  if(direccion == 1){
  inPin1 = HIGH;
  inPin2 = LOW;
  }

  if(motor == 1){
  digitalWrite(AIN1, inPin1);
  digitalWrite(AIN2, inPin2);
  analogWrite(PWMA, velocidad);
  }else{
  digitalWrite(BIN1, inPin1);
  digitalWrite(BIN2, inPin2);
  analogWrite(PWMB, velocidad);
  }
}

void stop(){
//habilita standby
digitalWrite(STBY, LOW);
}

    
    void Caminar(){
      //el robot va hacia adelante
      //200 180
    mover(1, 255, 0); //motor 1,  speed, right
    mover(2, 255, 0); //motor 2,  speed, right
    }
    void Retroceso(){
      //El robot avanza hacia atras
      mover(1, 255, 1); //motor 1, full speed, left
      mover(2, 255, 1); //motor 2, full speed, left
    }
    void Detener(){
      //Robot se detiene
      stop(); //stop
    }
    void GiraDerecha(){
      //El robot gira hacia la derecha
      mover(1, 0, 0); //motor 1, full speed, left
      mover(2, 255, 1); //motor 2, full speed, left
    }
    void GiraIzquierda(){
      //el robot Gira a la izquierda
      mover(1, 255, 1); //motor 1, full speed, left
      mover(2, 0, 0); //motor 2, full speed, left
    }

 void Normal(){

 //erase all figures
  TFTscreen.background(192, 192, 192);
  
  //generate a random color
  int redRandom = random(100, 255);
  int greenRandom = random (100, 255);
  int blueRandom = random (100, 255);
  
   TFTscreen.background(redRandom, greenRandom, blueRandom);
  // set the color for the figures
  
  
  //draw a circle
  TFTscreen.fill(128, 128, 128); // set the fill color to blue
  TFTscreen.stroke(0, 0, 0);
  TFTscreen.circle(35,31,18);
  TFTscreen.circle(125,31,18);
  
  TFTscreen.fill(255, 255, 255);
  TFTscreen.noStroke();
  TFTscreen.rect(25,20,13,5);
  TFTscreen.rect(40,25,5,5);
  TFTscreen.rect(25,25,5,7);
  TFTscreen.rect(25,34,5,5);
  
  TFTscreen.rect(115,20,13,5);
  TFTscreen.rect(130,25,5,5);
  TFTscreen.rect(115,25,5,7);
  TFTscreen.rect(115,34,5,5);
  
  TFTscreen.fill(0, 0, 0);
  TFTscreen.noStroke();
  int pos=-2;
  //TFTscreen.rect(10,30,5,5);
  TFTscreen.rect(20+pos,13,5,5);
  TFTscreen.rect(25+pos,8,5,5);
  TFTscreen.rect(30+pos,5,5,5);
  TFTscreen.rect(32+pos,5,10,5);
  TFTscreen.rect(40+pos,5,5,5);
  TFTscreen.rect(44+pos,8,5,5);
  TFTscreen.rect(49+pos,13,5,5);
  //TFTscreen.rect(49,30,5,5);

  //TFTscreen.rect(100,30,5,5);
  TFTscreen.rect(110+pos,13,5,5);
  TFTscreen.rect(115+pos,8,5,5);
  TFTscreen.rect(120+pos,5,5,5);
  TFTscreen.rect(122+pos,5,10,5);
  TFTscreen.rect(130+pos,5,5,5);
  TFTscreen.rect(134+pos,8,5,5);
  TFTscreen.rect(139+pos,13,5,5);
  //TFTscreen.rect(139,30,5,5);
  //delay(500);
  
  TFTscreen.fill(255, 255, 255); // set the fill color to white
    // light up a single point
  //TFTscreen.point(80,64);
  // wait 200 miliseconds until change to next figure
  //delay(500);

  // draw a line
/*  TFTscreen.line(65,60,80,30);
 // delay(500);

  // draw a line
  TFTscreen.line(65,60,80,60);*/
 // delay(500);

  //draw a square
  TFTscreen.noStroke(); // don't draw a line around the next rectangle x,y,largo, alto
  //13
  TFTscreen.rect(45,80,70,13);
   TFTscreen.fill(100, 100, 100);
   
 /* TFTscreen.rect(40,85,10,15);
  TFTscreen.rect(110,85,10,15);
  TFTscreen.rect(35,95,10,15);
  TFTscreen.rect(115,95,10,15);*/

  TFTscreen.rect(45,93,70,25);
  
  TFTscreen.fill(255, 151, 151);
  //Lengua
  TFTscreen.rect(55,105,49,13);
  /*TFTscreen.rect(78,95,10,15);
  
  TFTscreen.rect(65,98,10,12);
  TFTscreen.rect(80,98,10,12);*/

}

void Triste(){

  //erase all figures
  TFTscreen.background(192, 192, 192);
  
    //generate a random color
  int redRandom = random(100, 255);
  int greenRandom = random (100, 255);
  int blueRandom = random (100, 255);
  
   TFTscreen.background(redRandom, greenRandom, blueRandom);
  // set the color for the figures
  
  
  //draw a circle
  TFTscreen.fill(128, 128, 128); // set the fill color to blue
  TFTscreen.stroke(0, 0, 0);
  TFTscreen.circle(35,35,18);
  TFTscreen.circle(125,35,18);
  
  TFTscreen.fill(0, 0, 0);
  TFTscreen.noStroke();
  TFTscreen.fill(255, 255, 255);
  TFTscreen.rect(35,21,10,10);
  TFTscreen.fill(0, 0, 0);
  TFTscreen.rect(30,21,5,11);  
  TFTscreen.rect(35,32,10,5);
  TFTscreen.fill(192, 192, 192);
  TFTscreen.rect(30,32,5,5);
  
  TFTscreen.fill(255, 255, 255);
  TFTscreen.rect(125,21,10,10);
  TFTscreen.fill(0, 0, 0);
  TFTscreen.rect(120,21,5,11);
  TFTscreen.rect(125,32,10,5);
  TFTscreen.fill(192, 192, 192);
  TFTscreen.rect(120,32,5,5);
  
  TFTscreen.fill(0, 0, 0);
  TFTscreen.noStroke();
  int pos=-2;
  int pos2=0;
  //TFTscreen.rect(10,30,5,5);
  TFTscreen.rect(30+pos,10+pos2,5,5);
  TFTscreen.rect(32+pos,10+pos2,10,5);
  TFTscreen.rect(40+pos,10+pos2,5,5);
  TFTscreen.rect(44+pos,7+pos2,5,5);

  //TFTscreen.rect(49,30,5,5);

  //TFTscreen.rect(100,30,5,5);

  TFTscreen.rect(115+pos,7+pos2,5,5);
  TFTscreen.rect(120+pos,10+pos2,5,5);
  TFTscreen.rect(122+pos,10+pos2,10,5);
  TFTscreen.rect(130+pos,10+pos2,5,5);
  //TFTscreen.rect(139,30,5,5);
  //delay(500);
  
  TFTscreen.fill(128, 128, 128); // set the fill color to white
    // light up a single point
  //TFTscreen.point(80,64);
  // wait 200 miliseconds until change to next figure
  //delay(500);

  // draw a line
/*  TFTscreen.line(65,60,80,30);
 // delay(500);

  // draw a line
  TFTscreen.line(65,60,80,60);*/
 // delay(500);

  //draw a square
  TFTscreen.noStroke(); // don't draw a line around the next rectangle x,y,largo, alto
  TFTscreen.rect(45,80,70,13);
  
  TFTscreen.rect(40,85,10,15);
  TFTscreen.rect(110,85,10,15);
  TFTscreen.rect(35,95,10,15);
  TFTscreen.rect(115,95,10,15);
  

  
}

void Alegre(){
  
 //erase all figures
  TFTscreen.background(192, 192, 192);
  
  //generate a random color
  int redRandom = random(100, 255);
  int greenRandom = random (100, 255);
  int blueRandom = random (100, 255);
  TFTscreen.background(redRandom, greenRandom, blueRandom);
  // set the color for the figures
  TFTscreen.stroke(redRandom, greenRandom, blueRandom);
  
  //draw a circle
  TFTscreen.fill(redRandom, greenRandom, blueRandom); // set the fill color to blue
 /* TFTscreen.circle(35,30,25);
  TFTscreen.circle(125,30,25);*/
  
  TFTscreen.fill(0, 0, 0);
  TFTscreen.noStroke();
  TFTscreen.rect(10,30,12,12);
  TFTscreen.rect(15,20,12,12);
  TFTscreen.rect(20,15,12,12);
  TFTscreen.rect(25,12,12,12);
  TFTscreen.rect(27,12,12,12);
  TFTscreen.rect(35,12,12,12);
  TFTscreen.rect(39,15,12,12);
  TFTscreen.rect(44,20,12,12);
  TFTscreen.rect(49,30,12,12);

  TFTscreen.rect(100,30,12,12);
  TFTscreen.rect(105,20,12,12);
  TFTscreen.rect(110,15,12,12);
  TFTscreen.rect(115,12,12,12);
  TFTscreen.rect(117,12,12,12);
  TFTscreen.rect(125,12,12,12);
  TFTscreen.rect(129,15,12,12);
  TFTscreen.rect(134,20,12,12);
  TFTscreen.rect(139,30,12,12);
  //delay(500);
  
  TFTscreen.fill(255, 255, 255); // set the fill color to white
    // light up a single point
  //TFTscreen.point(80,64);
  // wait 200 miliseconds until change to next figure
  //delay(500);

  // draw a line
/*  TFTscreen.line(65,60,80,30);
 // delay(500);

  // draw a line
  TFTscreen.line(65,60,80,60);*/
 // delay(500);

  //draw a square
  TFTscreen.noStroke(); // don't draw a line around the next rectangle x,y,largo, alto
  //13
  TFTscreen.rect(40,75,80,13);
   TFTscreen.fill(100, 100, 100);
   
 /* TFTscreen.rect(40,85,10,15);
  TFTscreen.rect(110,85,10,15);
  TFTscreen.rect(35,95,10,15);
  TFTscreen.rect(115,95,10,15);*/

  TFTscreen.rect(40,85,80,23);
  TFTscreen.rect(45,108,70,10);
  TFTscreen.fill(255, 151, 151);
  //Lengua
  TFTscreen.rect(55,95,49,23);
  /*TFTscreen.rect(78,95,10,15);
  
  TFTscreen.rect(65,98,10,12);
  TFTscreen.rect(80,98,10,12);*/
}

