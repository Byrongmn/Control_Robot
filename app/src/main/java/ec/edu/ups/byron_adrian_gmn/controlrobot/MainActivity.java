package ec.edu.ups.byron_adrian_gmn.controlrobot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button ArribaDer;
    private Button AbajoDer;
    private Button ArribaIz;
    private Button AbajoIz;
    private Button Parar;
    private Button Adelante;
    private Button Retroceder;
    private Button Gderecha;
    private Button Gizquierda;
    private Button Alegre;
    private Button Normal;
    private Button Triste;

    //1)
    Button ServoI1, ServoD1, Desconectar1, Caminar1, Detener1, Retroceder1, Normal1, Alegre1, Triste1;
    TextView IdBufferIn;


    private static final String BUTTON_VIEW_TAG = "ARRASTRAR ";

    private Button btnPlay, btnlimpiar, btnSalir;


    //variables para l conexion del blutethoot
    public static Handler bluetoothIn;
    final static int handlerState = 0;
    public static BluetoothAdapter btAdapter = null;
    public static BluetoothSocket btSocket = null;
    public static StringBuilder DataStringIN = new StringBuilder();
    public static MainActivity.ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    public static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    public static String address = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        implementEvents();


        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnSalir = (Button) findViewById(R.id.btnsalir);
        btnlimpiar = (Button) findViewById(R.id.btnlimpiar);
        btnlimpiar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("f");
                Intent intend = new Intent(getApplicationContext(), DispositivosBT1.class);
                startActivity(intend);
                Toast.makeText(getBaseContext(), "LIMPIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                vaciar();

            }
        });
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < arreglo.length; i++) {
                    Log.d("Valor del arreglo", "" + arreglo[i]);

                }
                ejecutarComandos();
            }
        });
        btnSalir.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("entroooo");

                MyConexionBT.write("3");
                Intent intend = new Intent(getApplicationContext(), DispositivosBT.class);
                startActivity(intend);
                Toast.makeText(getBaseContext(), "SALIO A CONTROL", Toast.LENGTH_SHORT).show();


            }
        });
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndex = DataStringIN.indexOf("#");

                    if (endOfLineIndex > 0) {
                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
//                        IdBufferIn.setText("Dato: " + dataInPrint);//<-<- PARTE A MODIFICAR >->->
                        DataStringIN.delete(0, DataStringIN.length());
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        VerificarEstadoBT();


    }


    public BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);

    }


    @Override
    public void onResume() {
        super.onResume();
        //Consigue la direccion MAC desde DeviceListActivity via intent
        Intent intent = getIntent();
        //Consigue la direccion MAC desde DeviceListActivity via EXTRA
        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);//<-<- PARTE A MODIFICAR >->->
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try {
            btSocket.connect();
//            enableButtons (btSocket.isConnected());
            Toast.makeText(getBaseContext(), "Conexion Exitosa", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            try {
                btSocket.close();
                Toast.makeText(getBaseContext(), "Conexion Fallidasaa", Toast.LENGTH_SHORT).show();

            } catch (IOException e2) {
            }
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        try { // Cuando se sale de la aplicación esta parte permite
            // que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    //Comprueba que el dispositivo Bluetooth Bluetooth está disponible y solicita que se active si está desactivado
    public void VerificarEstadoBT() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //Crea la clase que permite crear el evento de conexion
    public class ConnectedThread extends Thread {
        public final InputStream mmInStream;
        public final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //Envio de trama
        public void write(String input) {
            try {
                mmOutStream.write(input.getBytes());
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo es main", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    //Encuentra todas las vistas y configura Tag para todas las vistas que se pueden arrastrar
    private void findViews() {


        ArribaDer = (Button) findViewById(R.id.btnbarrder);
        ArribaDer.setTag(BUTTON_VIEW_TAG);

        AbajoDer = (Button) findViewById(R.id.btnbader);
        AbajoDer.setTag(BUTTON_VIEW_TAG);

        ArribaIz = (Button) findViewById(R.id.btnbarrizq);
        ArribaIz.setTag(BUTTON_VIEW_TAG);

        AbajoIz = (Button) findViewById(R.id.btnbaizq);
        AbajoIz.setTag(BUTTON_VIEW_TAG);

        Parar = (Button) findViewById(R.id.btnparar);
        Parar.setTag(BUTTON_VIEW_TAG);

        Adelante = (Button) findViewById(R.id.btnade);
        Adelante.setTag(BUTTON_VIEW_TAG);

        Retroceder = (Button) findViewById(R.id.btnretro);
        Retroceder.setTag(BUTTON_VIEW_TAG);

        Gderecha = (Button) findViewById(R.id.btngder);
        Gderecha.setTag(BUTTON_VIEW_TAG);

        Gizquierda = (Button) findViewById(R.id.btngizq);
        Gizquierda.setTag(BUTTON_VIEW_TAG);

        Alegre = (Button) findViewById(R.id.btnalegre);
        Alegre.setTag(BUTTON_VIEW_TAG);

        Normal = (Button) findViewById(R.id.btnnormal);
        Normal.setTag(BUTTON_VIEW_TAG);

        Triste = (Button) findViewById(R.id.btnTriste);
        Triste.setTag(BUTTON_VIEW_TAG);
    }


    //Implementar un escucha largo y hacer clic y arrastrar
    private void implementEvents() {
        //agrega o elimina cualquier vista que no quieras que se arrastre


        ArribaDer.setOnLongClickListener(this);
        AbajoDer.setOnLongClickListener(this);
        ArribaIz.setOnLongClickListener(this);
        AbajoIz.setOnLongClickListener(this);
        Parar.setOnLongClickListener(this);
        Adelante.setOnLongClickListener(this);
        Retroceder.setOnLongClickListener(this);
        Gderecha.setOnLongClickListener(this);
        Gizquierda.setOnLongClickListener(this);
        Alegre.setOnLongClickListener(this);
        Normal.setOnLongClickListener(this);
        Triste.setOnLongClickListener(this);


        //agregue o elimine cualquier vista de diseño que no quiera aceptar vista arrastrada
        findViewById(R.id.top_layout).setOnDragListener(this);
        findViewById(R.id.left_layout).setOnDragListener(this);

    }

    @Override
    public boolean onLongClick(View view) {
        // Crea un nuevo ClipData.
        // Esto se hace en dos pasos para proporcionar claridad. El método de conveniencia
        // ClipData.newPlainText () puede crear un ClipData de texto sin formato en un solo paso.

        //Crea un nuevo ClipData.Item a partir de la etiqueta del objeto ImageView

        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        // Crea un nuevo ClipData usando la etiqueta como una etiqueta, el tipo de texto plano MIME, y
        // el elemento ya creado. Esto creará un nuevo objeto ClipDescription dentro del
        // ClipData, y establece su entrada de tipo MIME en "texto / plano"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        // Crea una instancia del generador de sombras de arrastre.
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        // Inicia el arrastre
        view.startDrag(data ///datos a arrastrar
                , shadowBuilder //sombra de arrastre
                , view//datos locales sobre la operación de arrastrar y soltar
                , 0//no necesita flags
        );

        //Establecer visibilidad de vista a INVISIBLE ya que vamos a arrastrar la vista
        view.setVisibility(View.INVISIBLE);
        return true;
    }

    // Este es el método que el sistema llama cuando distribuye un evento de arrastre al
    // oyente
    @Override
    public boolean onDrag(View view, DragEvent event) {
        // Define una variable para almacenar el tipo de acción para el evento entrante
        int action = event.getAction();
        // Maneja cada uno de los eventos esperados
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determina si esta vista puede aceptar los datos arrastrados
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // si desea aplicar color cuando el arrastre comenzó a su vista, puede descomentar las líneas siguientes
                    // para dar cualquier tinte de color a la Vista para indicar que puede aceptar datos.

                    view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);//establecer el color de fondo para su vista

                    // Invalidar la vista para forzar un redibujado en el nuevo color
                    //  view.invalidate();

                    // devuelve verdadero para indicar que la vista puede aceptar los datos arrastrados.
                    return true;

                }

                // Devuelve falso. Durante la operación actual de arrastrar y soltar, esta vista
                // no recibir eventos nuevamente hasta que se envíe ACTION_DRAG_ENDED.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Aplica un AZUL o cualquier tinte de color a la Vista, cuando la vista arrastrada ingresó en la vista arrastrar aceptable
                // Devuelve verdadero; El valor de retorno es ignorado.

                view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                // Invalidar la vista para forzar un redibujado en el nuevo tinte
                view.invalidate();

                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore el evento
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                // Vuelve a establecer el tono del color en azul, si configuró el color AZUL o cualquier color en ACTION_DRAG_STARTED. Devuelve verdadero; El valor de retorno es ignorado.
                //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                //Si no ha proporcionado ningún color en ACTION_DRAG_STARTED, borre el filtro de color.
                view.getBackground().clearColorFilter();
                // Invalidar la vista para forzar un redibujado en el nuevo tinte
                view.invalidate();

                return true;
            case DragEvent.ACTION_DROP:
                // Obtiene el elemento que contiene los datos arrastrados
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Obtiene los datos de texto del elemento.
                String dragData = item.getText().toString();

                // Muestra un mensaje que contiene los datos arrastrados.
                //Toast.makeText(this, "ARRASTRADO OK" + dragData, Toast.LENGTH_SHORT).show();

                // Desactiva los tintes de color
                view.getBackground().clearColorFilter();

                // Invalida la vista para forzar un redibujado
                view.invalidate();

                View v = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);//eliminar la vista arrastrada
                LinearLayout container = (LinearLayout) view;//crea la vista en LinearLayout ya que nuestro diseño aceptable para arrastrar es LinearLayout
                container.addView(v);//Agregar la vista arrastrada
                this.IdElemento(v.getId());
                v.setVisibility(View.VISIBLE);//finalmente establece Visibilidad en VISIBLE

                //Devuelve verdadero. DragEvent.getResult () devolverá verdadero.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                // Desactiva cualquier tinte de color
                view.getBackground().clearColorFilter();

                // Invalida la vista para forzar un redibujado
                //view.invalidate();


                if (event.getResult())
                    //Toast.makeText(this, "se solto bien", Toast.LENGTH_SHORT).show();

                    // else
                    // Toast.makeText(this, "no funciono", Toast.LENGTH_SHORT).show();


                    // devuelve verdadero; el valor es ignorado
                    return true;

                // Se recibió un tipo de acción desconocida.
            default:
                Log.e("Ejemplo", "Tipo de acción desconocido recibido por OnDragListener.");
                break;
        }
        return false;
    }

    String[] arreglo = new String[16];
    String val;
    int contador = 0;

    public void IdElemento(int num) {
        //if (num == R.id.btnTriste)
        if (contador < 16) {
            switch (num) {

                case R.id.btnbarrizq:
                    Log.d("Elemento id:", "Brazo arriba izquierda");
                    arreglo[contador] = "a";
                    contador++;
                    break;
                case R.id.btnbaizq:
                    Log.d("Elemento id:", "Brazo baja izquierda");
                    arreglo[contador] = "b";
                    contador++;
                    break;
                case R.id.btnbarrder:
                    Log.d("Elemento id:", "Brazo arriba derecha");
                    arreglo[contador] = "c";
                    contador++;
                    break;
                case R.id.btnbader:
                    Log.d("Elemento id:", "Brazo abajo derecha");
                    arreglo[contador] = "d";
                    contador++;
                    break;
                case R.id.btnade:
                    Log.d("Elemento id:", "Adelante");
                    arreglo[contador] = "e";
                    contador++;
                    arreglo[contador] = "f";
                    contador++;
                    break;
                case R.id.btnparar:
                    Log.d("Elemento id:", "Parar");
                    arreglo[contador] = "f";
                    contador++;
                    break;

                case R.id.btnretro:
                    Log.d("Elemento id:", "Retroceder");
                    arreglo[contador] = "g";
                    contador++;
                    arreglo[contador] = "f";
                    contador++;
                    break;
                case R.id.btngizq:
                    Log.d("Elemento id:", "Girar Izquierda");
                    arreglo[contador] = "h";
                    contador++;
                    arreglo[contador] = "f";
                    contador++;
                    break;
                case R.id.btngder:
                    Log.d("Elemento id:", "Girar Derecha");
                    arreglo[contador] = "i";
                    contador++;
                    arreglo[contador] = "f";
                    contador++;
                    break;
                case R.id.btnnormal:
                    Log.d("Elemento id:", "Normal");
                    arreglo[contador] = "j";
                    contador++;
                    break;
                case R.id.btnalegre:
                    Log.d("Elemento id:", "Alegre");
                    arreglo[contador] = "k";
                    contador++;
                    break;
                case R.id.btnTriste:
                    Log.d("Elemento id:", "Triste");
                    arreglo[contador] = "l";
                    contador++;
                    break;
                default:
                    Log.e("error", "Otro valor hallado");
                    break;
            }
        }
    }

    public void ejecutarComandos() {
        for (int i = 0; i < arreglo.length; i++) {
            final String Comando = arreglo[i];
                String val = Comando;

                    if (val != null) {
                        System.out.println("COMANDO          "+val);
                        MyConexionBT.write(val);
                        if (val != "f") {
                            SystemClock.sleep(3000);
                        }else{
                            SystemClock.sleep(1000);
                        }
                    }
        }
        Toast.makeText(getBaseContext(), "Comandos Ejecutados", Toast.LENGTH_SHORT).show();
    }

    public void vaciar() {

        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i] = null;
        }
    }


}