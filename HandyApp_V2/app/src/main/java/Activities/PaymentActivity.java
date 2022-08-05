package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.handyapp_v2.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PaymentActivity extends AppCompatActivity {

    CardView addPayment,creditcard;
    TextView amount, creditCardText, title;
    int REQUEST_CODE = 0077;
    Button checkOut;
    String name, email, price;
    String sendermail;

    public String stripe_token = "pk_test_51KaeHVH2uQT2W8eXtF7YgUv6J1T617qWJ73hbQ3HxMpldJDD1H7CGvMplRiNhjq9UsY6B0NjlZuEJ40m8CL7vIGk00cWeYkRyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        sendermail = getIntent().getStringExtra("address");

        addPayment = (CardView) findViewById(R.id.addPayment);
        creditCardText = (TextView) findViewById(R.id.credit_card_text);
        title = (TextView) findViewById(R.id.textView);
        checkOut = (Button) findViewById(R.id.checkout);

        title.setText("Sending $"+price+" to "+name+" please add payment method to confirm;");

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendemail();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        addPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddPayment.class);
                startActivityForResult(intent,REQUEST_CODE);//depricated method until we find the right alternative.
            }
        });

    }

    private void sendemail() {
        final String username="handyappservice@gmail.com";
        final String password="Handy2022-";

//        String messageToSend =

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Received Payment");
            message.setText("Hello "+name+"\nYou Have Received "+price+" into your BankAccount From " +sendermail);
            //Transport.send(message);

            Message message1 = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sendermail"));
            message.setSubject("Payment Successfully Send");
            message.setText("You have Send "+price+" to "+name);
            Transport.send(message1);


            Toast.makeText(PaymentActivity.this, "Email Send", Toast.LENGTH_SHORT).show();




        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PaymentActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == resultCode){

            creditcard.setVisibility(View.VISIBLE);
            stripe_token = data.getStringExtra("stripe_token");

            if(stripe_token.length()>1)
                checkOut.setVisibility(View.VISIBLE);

            creditCardText.setText(data.getStringExtra("cardtype")+" card ending with "+data.getStringExtra("card"));

        }

    }
}