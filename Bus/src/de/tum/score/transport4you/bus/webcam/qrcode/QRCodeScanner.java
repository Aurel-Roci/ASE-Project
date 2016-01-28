package de.tum.score.transport4you.bus.webcam.qrcode;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketDummy;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.QRCodedObject;


public class QRCodeScanner extends JFrame implements Runnable, ThreadFactory {

	private static final long serialVersionUID = 6441489157408381878L;

	private Executor executor = Executors.newSingleThreadExecutor(this);

	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;

	public QRCodeScanner() {
		super();

		setLayout(new FlowLayout());
		setTitle("Read QR / Bar Code With Webcam");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension size = WebcamResolution.QVGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);

		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);

		textarea = new JTextArea(1, 3);
		textarea.setEditable(false);
		textarea.setPreferredSize(size);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
	
		add(panel);
		add(textarea);

		pack();
		setVisible(true);

		executor.execute(this);
	}

	public void run() {

		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Result result = null;
			BufferedImage image = null;

			if (webcam.isOpen()) {

				if ((image = webcam.getImage()) == null) {
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			if (result != null ) {
				textarea.setText(result.getText());
				System.out.println("QRCode Text:\n" + result.getText());
				ETicket ticket = ETicket.deserialize(result.getText()); //recover the ETicket
				QRCodedObject ticketEncrypted = QRCodedObject.deserialize(result.getText()); 
				/*if (result == ticketEncrypted){
					
				}*/
			}

		} while (true);
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}

	public static void main(String[] args) {
		new QRCodeScanner();
	}
}