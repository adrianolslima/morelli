package partesInterface;

import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import cip.NonComponentPortOutbox;
import classes.JogadaMorelli;
import classes.TipoJogada;
import interfaces.NGProxy;

public class PortNGOutbox extends NonComponentPortOutbox implements NGProxy {

	public void initialize() {
		
		Proxy proxy = Proxy.getInstance();
		this.connectNonComponent(proxy);
	}
	
	public void connectToNG(PortNG portNG) {
		
		((Proxy) externalReference).addOuvinte(portNG);	
	}
	
	@Override
	public boolean conectar(String ip, String nome) {

		try {
			
			((Proxy) externalReference).conectar(ip, nome);
			return true; 
			
		} catch (Exception e) {
			
			return false;
		}
	}

	@Override
	public boolean desconectar() {

		try {
			
			((Proxy) externalReference).desconectar();
			return false;
			
		} catch (NaoConectadoException e) {

			return true;
		}
	}

	@Override
	public void iniciarPartida() {

		try {
    		
			((Proxy) externalReference).iniciarPartida(2);
            
        } catch (Exception e) {

        	System.out.println(e.getStackTrace());
        }
	}

	@Override
	public void enviarJogada(JogadaMorelli jogada) {

		try {
    		
            ((Proxy) externalReference).enviaJogada(jogada);
//            return true;
            
        } catch (Exception e) {
        	
//            return false;
        }
	}

	@Override
	public void reiniciarPartida() {

		try {

			((Proxy) externalReference).reiniciarPartida();
		
		} catch (Exception e) {

			System.out.println("TEstando!");
		}
	}

}
