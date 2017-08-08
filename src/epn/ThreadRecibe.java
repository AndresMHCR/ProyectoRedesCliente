package epn;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadRecibe implements Runnable {
    private final PrincipalChat main;
    private String mensaje; 
    private ObjectInputStream entrada;
    private Socket cliente;
   
    
   //Inicializar chatServer y configurar GUI
   public ThreadRecibe(Socket cliente, PrincipalChat main){
       this.cliente = cliente;
       this.main = main;
   }  

    public void mostrarMensaje(String mensaje) {
        main.areaTexto.append(mensaje);
    } 
   
    public void run() {
    	String key = "92AE31A79FEEB2A3"; //llave
        String iv = "0123456789ABCDEF"; // vector de inicialización
        try {
            entrada = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecibe.class.getName()).log(Level.SEVERE, null, ex);
        }
        do { //procesa los mensajes enviados desde el servidor
        	
            try {//leer el mensaje y mostrarlo 
                mensaje = (String) entrada.readObject(); //leer nuevo mensaje
                System.out.println(mensaje);
                main.mostrarMensaje("Servidor>>>"+epn.StringEncrypt.decrypt(key, iv, mensaje));
            } //fin try
            catch (SocketException ex) {
            }
            catch (EOFException eofException) {
                main.mostrarMensaje("Fin de la conexion");
                break;
            } //fin catch
            catch (IOException ex) {
                Logger.getLogger(ThreadRecibe.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException classNotFoundException) {
                main.mostrarMensaje("Objeto desconocido");
            } //fin catch               
 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print("hola");
			}

        } while (!mensaje.equals("Cliente>>> TERMINATE")); //Ejecuta hasta que el server escriba TERMINATE

        try {
            entrada.close(); //cierra entrada Stream
            cliente.close(); //cierra Socket
        } //Fin try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } //fin catch

        main.mostrarMensaje("Fin de la conexion");
        System.exit(0);
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