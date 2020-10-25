package com.example.reminderlist;

import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {
    TextInputLayout txt_layEmail,txt_layPass;
    TextView txt_forgot,txt_signup;
    Button btn_login;
    String email,pass;
    FirebaseAuth auth;
    FirebaseFirestore db;



    public LoginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        txt_layEmail=view.findViewById(R.id.outlinedTextField_email);
        txt_layPass=view.findViewById(R.id.outlinedTextField_pass);
        btn_login=view.findViewById(R.id.containedButton_login);
        txt_forgot=view.findViewById(R.id.txt_forgotPass);
        txt_signup=view.findViewById(R.id.txt_signUp);
        btn_login.setOnClickListener(login);
        txt_forgot.setOnClickListener(forgot);
        txt_signup.setOnClickListener(newUser);

    }


    View.OnClickListener forgot=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_main);
            navController.navigate(R.id.forgotFragment);
        }
    };

    View.OnClickListener login=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            email=txt_layEmail.getEditText().getText().toString().trim();
            pass=txt_layPass.getEditText().getText().toString().trim();

            if (!checkEmptyField()){
                AuthCredential credential = EmailAuthProvider.getCredential(email,pass);

                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            checkIfAdmin(auth.getCurrentUser().getUid());

                        }else{
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                Toast.makeText(getActivity().getApplicationContext(),"Email not exist!",Toast.LENGTH_LONG).show();
                                txt_layEmail.getEditText().getText().clear();
                                txt_layPass.getEditText().getText().clear();
                                txt_layEmail.setError("Email not exist!");
                                txt_layEmail.getEditText().requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                // Toast.makeText(getActivity().getApplicationContext(),"Wrong Password!",Toast.LENGTH_LONG).show();
                                txt_layEmail.getEditText().getText().clear();
                                txt_layPass.getEditText().getText().clear();
                                txt_layEmail.setError("Please enter valid login credentials");
                                txt_layEmail.getEditText().requestFocus();
                            }catch (Exception e ){
                                Toast.makeText(getActivity().getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

            }

        }
    };

    private void checkIfAdmin(String uid) {

        db.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("isAdmin")!=null){
                    Toast.makeText(getActivity().getApplicationContext(),"Admin Login Success!",Toast.LENGTH_LONG).show();
                }else  {
                    Toast.makeText(getActivity().getApplicationContext(),"User Login Success!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    View.OnClickListener newUser=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_main);

            navController.navigate(R.id.registerFragment);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public boolean checkEmptyField() {

        if (TextUtils.isEmpty(email)) {
            txt_layEmail.setError("Email Cannot be Empty!");
            txt_layEmail.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(pass)) {
            txt_layEmail.setError(null);
            txt_layEmail.setErrorEnabled(false);
            txt_layPass.setError("Password Cannot be Empty!");
            txt_layPass.requestFocus();
            return true;
        }else if (pass.length()<6){
            txt_layPass.getEditText().getText().clear();
            txt_layPass.setError("Password Cannot less than 6 characters");
            txt_layPass.requestFocus();
            return true;
        }
        txt_layPass.setErrorEnabled(false);
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if (user!=null){
            checkIfAdmin(user.getUid());

        }
    }
}