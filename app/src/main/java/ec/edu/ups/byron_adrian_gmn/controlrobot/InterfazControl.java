package ec.edu.ups.byron_adrian_gmn.controlrobot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class InterfazControl extends AppCompatActivity {

    //1)
    Button ServoI, ServoD, Desconectar, Caminar, Detener, Retroceder, Normal, Alegre, Triste;
    TextView IdBufferIn;
    public int[] tabIcons = {R.drawable.cabeza, R.drawable.brazo, R.drawable.camina, R.drawable.puzle};
    //-------------------------------------------
    public static Handler bluetoothIn;
    final static int handlerState = 0;
    public static BluetoothAdapter btAdapter = null;
    public static BluetoothSocket btSocket = null;
    public static StringBuilder DataStringIN = new StringBuilder();
    public static ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    public static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    public static String address = null;
    //-------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_control);

        //2)
        //Enlaza los controles con sus respectivas vistas
        ServoI = (Button) findViewById(R.id.Arribabtn);
        Caminar = (Button) findViewById(R.id.Caminarbtn);
        Detener = (Button) findViewById(R.id.Pararbtn);
        Retroceder = (Button) findViewById(R.id.Retrocederbtn);
        Desconectar = (Button) findViewById(R.id.Desconectarbtn);
        Normal = (Button) findViewById(R.id.Normalbtn);
        Alegre = (Button) findViewById(R.id.Alegrebtn);
        Triste = (Button) findViewById(R.id.Tristebtn);
        IdBufferIn = (TextView) findViewById(R.id.BufferCabezatxt);


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

        // Configuracion onClick listeners para los botones
        // para indicar que se realizara cuando se detecte
        // el evento de Click
/*        ServoI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("1");
            }
        });

        ServoD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("0");
            }
        });/*
        Caminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("2");
            }
        });
        Detener.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("3");
            }
        });
        Retroceder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("4");
            }
        });
        Normal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("5");
            }
        });
        Alegre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("6");
            }
        });
        Triste.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyConexionBT.write("7");
            }
        });
        Desconectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.close();
                        Toast.makeText(getBaseContext(), "Conexion Terminada", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
                finish();
            }
        });*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < 4; i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);

        }
        iconColor(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()), "#03A9F4");
        iconColor(tabLayout.getTabAt(1), "#E0E0E0");
        iconColor(tabLayout.getTabAt(2), "#E0E0E0");
        iconColor(tabLayout.getTabAt(3), "#4CAF50");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                iconColor(tab, "#03A9F4");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                iconColor(tab, "#E0E0E0");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void iconColor(TabLayout.Tab tab, String color) {
        tab.getIcon().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentCabeza();
                case 1:
                    return new FragmentBrazo();
                case 2:
                    return new FragmentCaminar();
                default:
                    return new Fragment_pasar_menu();

            }
        }

        @Override
        public int getCount() {
            return 4;
        }


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
                Toast.makeText(getBaseContext(), "Conexion Fallida", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getBaseContext(), "La Conexión fallo control", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    //Habilita o deshabilita los botones
    /*private void enableButtons(boolean isEnabled) {
        ServoI.setEnabled(isEnabled);
        ServoD.setEnabled(isEnabled);
        Caminar.setEnabled(isEnabled);
        Detener.setEnabled(isEnabled);
        Retroceder.setEnabled(isEnabled);
        Normal.setEnabled(isEnabled);
        Alegre.setEnabled(isEnabled);
        Triste.setEnabled(isEnabled);
    }*/


}