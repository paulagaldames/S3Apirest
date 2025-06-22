import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Web Service App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,250);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));

        //etiquetas label
        JLabel fechaLabel = new JLabel("Fecha:");
        JLabel monedaLabel = new JLabel("Moneda:");
        JLabel valorLabel = new JLabel("Valor:");

        //input
        JTextField fechaText = new JTextField();
        JTextField monedaText = new JTextField();
        JTextField valorText = new JTextField();

        JButton fetchButton = new JButton("Obtener Datos");

        //se agregan los componentes
        mainPanel.add(fechaLabel);
        mainPanel.add(fechaText);
        mainPanel.add(monedaLabel);
        mainPanel.add(monedaText);
        mainPanel.add(valorLabel);
        mainPanel.add(valorText);
        mainPanel.add(fetchButton);

        fetchButton.addActionListener((ActionEvent e) -> {
            try {
                //
                URL url = new URL("http://localhost:3000");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                con.disconnect();
                //se verifica por consola que muestre los datos del json
                System.out.println("Json: " + content);

                // Parsear JSON
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(content.toString());

                JSONArray tipoCambioArray = (JSONArray) json.get("tipo_cambio");

                JSONObject primerElemento = (JSONObject) tipoCambioArray.get(0);

                String fecha = primerElemento.get("fecha").toString();
                String moneda = primerElemento.get("moneda").toString();
                String valor = primerElemento.get("valor").toString();

                //se muestra los datos en cada input
                fechaText.setText(fecha);
                monedaText.setText(moneda);
                valorText.setText(valor);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al obtener datos: " + ex.getMessage());
            }
        });


        frame.add(mainPanel);
        frame.setVisible(true);

        }
    }
