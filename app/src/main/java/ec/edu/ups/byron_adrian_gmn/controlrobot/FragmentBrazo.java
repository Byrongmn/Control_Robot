package ec.edu.ups.byron_adrian_gmn.controlrobot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static ec.edu.ups.byron_adrian_gmn.controlrobot.InterfazControl.DataStringIN;
import static ec.edu.ups.byron_adrian_gmn.controlrobot.InterfazControl.bluetoothIn;
import static ec.edu.ups.byron_adrian_gmn.controlrobot.InterfazControl.handlerState;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBrazo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBrazo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBrazo extends Fragment implements View.OnClickListener {
    InterfazControl inter = new InterfazControl();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentBrazo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBrazo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBrazo newInstance(String param1, String param2) {
        FragmentBrazo fragment = new FragmentBrazo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Button Arriba, Abajo,Arriba2, Abajo2, Desconectar;
    TextView IdBufferIn;
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View myView = inflater.inflate(R.layout.fragmentbrazo, container, false);
        Arriba = (Button) myView.findViewById(R.id.Arribabtn);
        Arriba.setOnClickListener(this);
        Arriba2 = (Button) myView.findViewById(R.id.Arribabtn2);
        Arriba2.setOnClickListener(this);
        Abajo = (Button) myView.findViewById(R.id.Abajobtn);
        Abajo.setOnClickListener(this);
        Abajo2 = (Button) myView.findViewById(R.id.Abajobtn2);
        Abajo2.setOnClickListener(this);
        Desconectar = (Button) myView.findViewById(R.id.Desconectarbtn);
        Desconectar.setOnClickListener(this);
        IdBufferIn = (TextView) myView.findViewById(R.id.BufferBrazotxt);
        mContext = getActivity();
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndex = DataStringIN.indexOf("#");

                    if (endOfLineIndex > 0) {
                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
                        IdBufferIn.setText("Dato: " + dataInPrint);//<-<- PARTE A MODIFICAR >->->
                        DataStringIN.delete(0, DataStringIN.length());
                    }
                }
            }
        };
        return myView;
    }

    public void showToast(String msg) {
        if (FragmentBrazo.this.isVisible() && msg != null & mContext != null)
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Arribabtn:
                inter.MyConexionBT.write("a");
                System.out.println("Presiona Arriba ");

                break;
            case R.id.Abajobtn:
                inter.MyConexionBT.write("b");
                System.out.println("Presiona Abajo ");
                break;

            case R.id.Arribabtn2:
                inter.MyConexionBT.write("c");
                System.out.println("Presiona Arriba2 ");

                break;
            case R.id.Abajobtn2:
                inter.MyConexionBT.write("d");
                System.out.println("Presiona Abajo2 ");
                break;
            case R.id.Desconectarbtn:

                if (inter.btSocket != null) {
                    try {
                        inter.MyConexionBT.write("f");
                        inter.btSocket.close();
                        showToast("Conexion Terminada");
                        Intent intend = new Intent(getActivity(), DispositivosBT.class);
                        startActivity(intend);
                    } catch (IOException e) {
                        //                              Toast.makeText(inter.getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
                inter.finish();


                System.out.println("Presiona Deconectar ");
                break;
            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
