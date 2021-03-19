package firebasetest;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseFrame extends JFrame implements ChangeListener, ActionListener
{
    String text1 = "温度センサー";
    String text2 = "水位センサー";
    String text3 = "光センサー";

    JLabel label1 = new JLabel(this.text1, JLabel.CENTER);
    JLabel label2 = new JLabel(this.text2, JLabel.CENTER);
    JLabel label3 = new JLabel(this.text3, JLabel.CENTER);

    JSlider slider1 = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
    JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
    JSlider slider3 = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);

    JLabel figure1 = new JLabel("0");
    JLabel figure2 = new JLabel("0");
    JLabel figure3 = new JLabel("0");

    JButton button = new JButton("データを送信");

//    JTextField textField1 = new JTextField();
//    JTextField textField2 = new JTextField();
//    JTextField textField3 = new JTextField();

    public FirebaseFrame()
    {
        this.setTitle("Sample No.12");
        this.setSize(800, 400);

        JPanel panel_center = new JPanel();

        GridLayout layout = new GridLayout(4, 3);
        panel_center.setLayout(layout);

        panel_center.add(this.label1);
        panel_center.add(this.slider1);
        panel_center.add(this.figure1);

        panel_center.add(this.label2);
        panel_center.add(this.slider2);
        panel_center.add(this.figure2);

        panel_center.add(this.label3);
        panel_center.add(this.slider3);
        panel_center.add(this.figure3);

//        panel_center.add(new JScrollPane(this.textField1));
//        panel_center.add(new JScrollPane(this.textField2));
//        panel_center.add(new JScrollPane(this.textField3));

        this.setLayout(new BorderLayout());
        this.add(panel_center, BorderLayout.CENTER);
        this.add(this.button, BorderLayout.SOUTH);

        this.slider1.addChangeListener(this);
        this.slider2.addChangeListener(this);
        this.slider3.addChangeListener(this);

        this.button.addActionListener(this);

        // プッシュ通知受信用の「リスナー」を定義、インスタンス化。

        ValueEventListener listener = new ValueEventListener()
        {
          public void onDataChange(DataSnapshot snapshot)
          {
            displayValues(snapshot);
          }

          public void onCancelled(DatabaseError error)
          {
            System.out.println("データ受信がキャンセルされました。" + error.toString());
          }
        };

		// Firebase データベースの「ルートリファレンス」を取得。

		DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

		// Firebase からデータ受信できるようにスタンバイ。

		reference.child("key1").addValueEventListener(listener);
		reference.child("key2").addValueEventListener(listener);
		reference.child("key3").addValueEventListener(listener);
		reference.child("key4").addValueEventListener(listener);
		reference.child("folder").child("a").addValueEventListener(listener);
		reference.child("folder").child("b").addValueEventListener(listener);
		reference.child("folder").child("c").addValueEventListener(listener);
		reference.child("sliders").child("value1").addValueEventListener(listener);
		reference.child("sliders").child("value2").addValueEventListener(listener);
		reference.child("sliders").child("value3").addValueEventListener(listener);
    }

    /**
     * 下記は ChangeListenerインターフェースの実装です。
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        Object source = e.getSource();

        if(source==this.slider1)
        {
            int value = this.slider1.getValue();
            String text = Integer.toString(value);
            this.figure1.setText(text);
        }
        else if(source==this.slider2)
        {
            int value = this.slider2.getValue();
            String text = Integer.toString(value);
            this.figure2.setText(text);
        }
        else if(source==this.slider3)
        {
            int value = this.slider3.getValue();
            String text = Integer.toString(value);
            this.figure3.setText(text);
        }
    }

    /**
     * 下記は ActionListenerインターフェースの実装です。
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
    	// Firebase データベースの「ルートリファレンス」を取得。

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // Firebase データベースへの書き込み

        reference.child("key1").setValue("こんにちは！", null);
        reference.child("key2").setValue("ファイアーベース", null);
        reference.child("key3").setValue("よろしくお願いします！", null);
        reference.child("key4").setValue(new Date(), null);
        reference.child("folder").child("a").setValue("あああ", null);
        reference.child("folder").child("b").setValue("いいい", null);
        reference.child("folder").child("c").setValue("ううう", null);

		// 画面上のスライダーの数値を送信

		DatabaseReference reference_sliders = reference.child("sliders");

		int value1 = this.slider1.getValue();
		int value2 = this.slider2.getValue();
		int value3 = this.slider3.getValue();

		reference_sliders.child("value1").setValue(value1, null);
		reference_sliders.child("value2").setValue(value2, null);
		reference_sliders.child("value3").setValue(value3, null);
    }

    /**
     * Firebaseから受け取ったデータを画面に表示するためのメソッド。
     */
    private void displayValues(DataSnapshot snapshot)
    {
    	MySwingWorker worker = new MySwingWorker(snapshot);
    	worker.execute();
    }

    /**
     * 通信スレッドから受け取ったデータをGUIスレッドで処理するための SwingWorker の実装例。
     */
    private class MySwingWorker extends SwingWorker
    {
    	private DataSnapshot snapshot = null;

    	public MySwingWorker(DataSnapshot snapshot)
    	{
    		this.snapshot = snapshot;
    	}

    	@Override
    	protected Object doInBackground() throws Exception
    	{
    		return null;
    	}

    	@Override
    	protected void done()
    	{
           try
           {
        	   String key = this.snapshot.getKey();
        	   Object value = this.snapshot.getValue();

        	   System.out.println("Firebaseからデータを受信しました。 " + key + " = " + value);

        	   // ここに画面表示の処理を書きます。
        	   // ここに画面表示の処理を書きます。
        	   // ここに画面表示の処理を書きます。
        	   // ここに画面表示の処理を書きます。
        	   // ここに画面表示の処理を書きます。
           }
           catch (Exception e)
           {
        	   e.printStackTrace();
           }
       }
   }
}
