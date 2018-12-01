package partesInterface;

import cip.InterfacePort;
import classes.JogadaMorelli;
import estrutura.Tabuleiro;
import interfaces.LogicaProxy;

public class PortProxy extends InterfacePort implements LogicaProxy {

	protected Tabuleiro tabuleiro;
	
	public PortProxy(String id) {

		this.id = id;
	}

	@Override
	public void initialize() {

		outbox = new PortProxyOutbox();
	}

	@Override
	public void receberSolicitacaoInicio(int ordem) {

		tabuleiro.receberSolicitacaoInicio(ordem);
	}

	@Override
	public void receberJogada(JogadaMorelli jogada) {

		tabuleiro.receberJogada(jogada);
	}

	public void setReferenciaInterna(Tabuleiro tabuleiro) {

		this.tabuleiro = tabuleiro;
	}

}
