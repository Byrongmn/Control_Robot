package ec.edu.ups.byron_adrian_gmn.controlrobot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_pasar_menu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_pasar_menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pasar_menu extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button btnpasar;
    private Context mContext;

    public Fragment_pasar_menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_pasar_menu.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_pasar_menu newInstance(String param1, String param2) {
        Fragment_pasar_menu fragment = new Fragment_pasar_menu();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment_pasar_menu, container, false);
        btnpasar = (Button)myView.findViewById(R.id.btnPasar);
        btnpasar.setOnClickListener(this);




        return myView;
    }

    public void showToast(String msg) {
        if (Fragment_pasar_menu.this.isVisible() && msg != null & mContext != null)
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void onClick(View v)
    {
        switch (v.getId()) {

            case R.id.btnPasar:

            System.out.println("Presiona correcto ");
            Intent intend = new Intent(getActivity(), DispositivosBT1.class);
            startActivity(intend);
            showToast("A JUGAR");

            break;

            default:
                break;
        }

    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
