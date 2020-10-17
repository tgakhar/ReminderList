package com.example.reminderlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.transition.TransitionInflater;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotFragment extends Fragment {

    TextView txt_login, txt_title, txt_tag;
    TextInputLayout txt_layEmail;
    Button btn_forgot;
    String email;
    FirebaseAuth auth;
    LinearLayout layout_bottom;
    ImageView img_logo;


    public ForgotFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_layEmail=view.findViewById(R.id.edt_forgotEmail);
        img_logo=view.findViewById(R.id.img_forgotLogo);
    txt_login=view.findViewById(R.id.txt_forgotLogin);
    btn_forgot=view.findViewById(R.id.btn_forgot);
    txt_title=view.findViewById(R.id.txt_forgotTitle);
    txt_tag=view.findViewById(R.id.txt_forgotHello);
    layout_bottom=view.findViewById(R.id.layout_forgotBottom);
        btn_forgot.setOnClickListener(forgot);
        txt_login.setOnClickListener(login);
}


        View.OnClickListener forgot=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=txt_layEmail.getEditText().getText().toString().trim();

                if (!checkEmptyField()){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity().getApplicationContext(), "Password reset link sent successfully", Toast.LENGTH_SHORT).show();
                                NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_main);


                                navController.navigate(R.id.loginFragment);
                            }
                        }
                    });
                }


            }
        };

    View.OnClickListener login=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_main);


            navController.navigate(R.id.loginFragment);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot, container, false);
    }


    public boolean checkEmptyField() {

        if (TextUtils.isEmpty(email)) {
            txt_layEmail.setError("Email Cannot be Empty!");
            txt_layEmail.requestFocus();
            return true;
        }
        txt_layEmail.setErrorEnabled(false);
        return false;
    }
}