package partesInterface;

import cip.InterfacePort;
import classes.JogadaMorelli;
import estrutura.Controlador;
import interfaces.LogicaInterface;

public class PortInterface extends InterfacePort implements LogicaInterface {
	
	protected Controlador tabuleiro;
	
	public PortInterface(String id) {

		this.id = id;
	}

	@Override
	public void initialize() {

		outbox = new PortInterfaceOutbox();
	}

	@Override
	public boolean conectar(String ip, String nome) {

		return tabuleiro.conectar(ip, nome);
	}

	@Override
	public boolean desconectar() {

		return tabuleiro.desconectar();
	}

	@Override
	public String getAjuda() {

		return tabuleiro.getAjuda();
	}

	@Override
	public boolean iniciarPartida() {

		return tabuleiro.solicitarInicioPartida();
	}

	@Override
	public boolean reiniciarPartida() {
		
		return tabuleiro.reiniciarPartida();
	}

	@Override
	public void enviarJogada(JogadaMorelli jogada) {

		tabuleiro.enviarJogada(jogada);
	}

	public void setReferenciaInterna(Controlador tabuleiro) {

		this.tabuleiro = tabuleiro;
	}

}
