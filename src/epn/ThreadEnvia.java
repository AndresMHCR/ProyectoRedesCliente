package epn;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
        
public class ThreadEnvia implements Runnable {
    private final PrincipalChat main; 
    private ObjectOutputStream salida;
    private String mensaje;
    private Socket conexion; 
   
    public ThreadEnvia(Socket conexion, final PrincipalChat main){
        this.conexion = conexion;
        this.main = main;
        
        //Evento que ocurre al escribir en el campo de texto
        main.campoTexto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mensaje = event.getActionCommand();
                String key = "92AE31A79FEEB2A3"; //llave
                String iv = "0123456789ABCDEF"; // vector de inicialización
                try {
					enviarDatos(epn.StringEncrypt.encrypt(key, iv, mensaje));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //se envia el mensaje
                main.campoTexto.setText(""); //borra el texto del enterfield
            } //Fin metodo actionPerformed
        } 
        );//Fin llamada a addActionListener
    } 
    
   //enviar objeto a cliente 
   private void enviarDatos(String mensaje) throws Exception{
	   String key = "92AE31A79FEEB2A3"; //llave
       String iv = "0123456789ABCDEF"; // vector de inicialización
      try {
         salida.writeObject(mensaje);
         salida.flush(); //flush salida a cliente
         main.mostrarMensaje("Cliente>>> " + epn.StringEncrypt.decrypt(key, iv, mensaje));
      } //Fin try
      catch (IOException ioException){ 
         main.mostrarMensaje("Error escribiendo Mensaje");
      } //Fin catch  
      
   } //Fin metodo enviarDatos
   
   public String encriptar(String texto,String clave)
   {
       int tamtext=texto.length();
       int tamclav=clave.length();
       int temp,p=0;
       String encriptado="";
   /* Se crea un array de enteros que contendran los numeros que
      corresponde a los caracteres en Ascii de los String Texto y la Clave */

       int textoAscii[]= new int[tamtext];
       int claveAscii[]= new int[tamclav];

   /* Se guardan los caracteres de cada String en
      numeros correspondientes al Ascii           */
       for(int i=0;i<tamtext;i++)
         textoAscii[i] = texto.charAt(i);

       for(int i=0;i<tamclav;i++)
         claveAscii[i] = clave.charAt(i);

       //Se procede al ENCRIPTADO
      for(int i=0;i<tamtext;i++){
        p++;

        if(p >= tamclav)
         p=0;

        temp =textoAscii[i]+claveAscii[p];

        if (temp > 255)
        temp=temp-255;

        encriptado = encriptado + (char)temp;
       }

    return encriptado;
   }
   
   //manipula areaPantalla en el hilo despachador de eventos
    public void mostrarMensaje(String mensaje) {
        main.areaTexto.append(mensaje);
    } 
   
    public void run() {
         try {
            salida = new ObjectOutputStream(conexion.getOutputStream());
            salida.flush(); 
        } catch (SocketException ex) {
        } catch (IOException ioException) {
          ioException.printStackTrace();
        } catch (NullPointerException ex) {
        }
    }
    public String desencriptar(String texto,String clave){
        int tamtext=texto.length();
        int tamclav=clave.length();
        int temp,p=0;
        String desencriptado="";
    /* Se crea un array de enteros que contendran los numeros que
       corresponde a los caracteres en Ascii de los String Texto y la Clave */

        int textoAscii[]= new int[tamtext];
        int claveAscii[]= new int[tamclav];

    /* Se guardan los caracteres de cada String en
       numeros correspondientes al Ascii           */
        for(int i=0;i<tamtext;i++)
          textoAscii[i] = texto.charAt(i);

        for(int i=0;i<tamclav;i++)
          claveAscii[i] = clave.charAt(i);

        //Se procede al DESENCRIPTADO
        for(int i=0;i<tamtext;i++)
        {
         p++;

          if(p>=tamclav)
          p=0;

         temp=textoAscii[i]-claveAscii[p];

         if (temp < 0)
         temp=temp+256;

         desencriptado=desencriptado + (char)temp;
        }
     return desencriptado;
    }
   
}