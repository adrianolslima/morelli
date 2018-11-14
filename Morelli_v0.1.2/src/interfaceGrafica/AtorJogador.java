package interfaceGrafica;

import entidades.*;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class AtorJogador {

    protected NetGames netGames;
    protected Tabuleiro tabuleiro;
    protected TelaJogador tela;
    
    protected ResourceBundle msgs;
    protected Ajuda ajuda;

    protected boolean conectado;

    protected Jogador jogador;
    protected boolean daVez;

    protected Faixa[] tabuleiroAtualizado;
    protected Posicao posicaoOrigem;
    protected Posicao posicaoDestino;

    public AtorJogador(ResourceBundle msgs) {

        this.netGames = new NetGames(this);
        this.tabuleiro = new Tabuleiro(this, msgs);
        
        this.msgs = msgs;
        this.ajuda = new Ajuda();

        this.tela = new TelaJogador(this, msgs);
        this.tela.setVisible(true);

        this.conectado = false;

        this.jogador = null;
        this.daVez = false;

        this.posicaoOrigem = null;
        this.posicaoDestino = null;

    }

    public void setJogador(Jogador jogador) {
    	
        this.jogador = jogador;
    }

    public Jogador getJogador() {
    	
        return jogador;
    }

    public void setDaVez(boolean vez) {
    	
        this.daVez = vez;
    }

    public boolean isDaVez() {
    	
        return daVez;
    }

    public void iniciarPartida() {

    	if(conectado) {

    		if (tabuleiro.isPartidaEmAndamento()) {

    			tela.notificarPartidaEmAndamento();

    			if (confirmarReiniciarPartida()) {

    				abandonarPartida();
    				netGames.reiniciarPartida();
    			}

    		} else {

    			tabuleiro.setPartidaEmAndamento(true);
    			netGames.iniciarPartida();
    		}

    	} else {
        	
            tela.notificar(msgs.getString("YouAreDisconnected"));
    	}
    }

    public void receberSolicitacaoInicio(int ordem) {

        if (ordem == 1) {
        	
            tela.informar(msgs.getString("Name") + netGames.getNomeAdversario(1) + "\n");
            tela.informar(msgs.getString("Opponent") + netGames.getNomeAdversario(2) + "\n\n");
            tela.informar(msgs.getString("YouPlayWith") + " " + msgs.getString("WhiteStones") + ".\n");
            tela.informar(msgs.getString("YourOpponentStartsPlaying"));
            setDaVez(true);
            
        } else {
        	
            tela.informar(msgs.getString("Name") + ": " + netGames.getNomeAdversario(2) + "\n");
            tela.informar(msgs.getString("Opponent") + ": " + netGames.getNomeAdversario(1) + "\n\n");
            tela.informar(msgs.getString("YouPlayWith") + " " + msgs.getString("BlackStones") + ".\n");
            tela.informar(msgs.getString("YouStartPlaying"));
            setDaVez(false);
        }

        String nomeJogador1 = netGames.getNomeAdversario(1);
        String nomeJogador2 = netGames.getNomeAdversario(2);

        tabuleiroAtualizado = tabuleiro.iniciarPartida(jogador, ordem, nomeJogador1, nomeJogador2);

        if (ordem == 1) {
            enviarJogada(TipoJogada.atualizarTabuleiro);
            tela.atualizaTabuleiro(tabuleiroAtualizado);
        }

    }

    public void finalizarPartidaEmpate() {
        tabuleiro.setPartidaEmAndamento(false);
        tela.exibeMensagemEmpate();
    }

    public boolean confirmarReiniciarPartida() {
        int resposta = JOptionPane.showConfirmDialog(null,
                msgs.getString("DoYouWantToRestartTheMatch"), 
                msgs.getString("RestartMatch"),
                JOptionPane.YES_NO_OPTION);
        return resposta == JOptionPane.YES_OPTION;
    }

    public void realizarAcordo() {
        int resposta = JOptionPane.showConfirmDialog(null,
                msgs.getString("DoYouWantToMakeADealAndFinishTheMatch"),
                msgs.getString("YourOpponentAskedForADeal"), 
                JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            this.finalizarPartidaEmpate();
            enviarJogada(TipoJogada.acordoAceito);
        } else {
            enviarJogada(TipoJogada.acordoNegado);
        }
    }

    public String ajuda() {
    	
        return ajuda.getAjuda();
    }

    public void movimentarPeca(Posicao origem, Posicao destino) {
        try {
            if (daVez && tabuleiro.isPartidaEmAndamento()) {
                if (origem.getCor() == jogador.getCor()) {
                    posicaoOrigem = origem;
                    posicaoDestino = destino;

                    if (tabuleiro.calcularMovimento(origem, destino)) {
                        tabuleiro.calcularCaptura(destino);
                        tabuleiro.calcularTomadaTrono(destino);
                        tabuleiroAtualizado = tabuleiro.getTabuleiro();
                        enviarJogada(TipoJogada.atualizarTabuleiro);
                        tela.atualizaTabuleiro(tabuleiroAtualizado);
                    }
                }
            }
        } catch (Exception e) {
            notificar(e.toString() + " movimentarPeca");
        }
    }

    public void enviarJogada(TipoJogada tipoJogada) {

        if (daVez && tabuleiro.isPartidaEmAndamento()) {
            setDaVez(false);
            tela.informar(msgs.getString("WaitUntilYourOpponentHasPlayed"));
            JogadaMorelli jogada;

            if (null != tipoJogada) {
                switch (tipoJogada) {
                    case realizarAcordo:
                        jogada = new JogadaMorelli(TipoJogada.realizarAcordo);
                        netGames.enviarJogada(jogada);
                        break;
                    case acordoAceito:
                        jogada = new JogadaMorelli(TipoJogada.acordoAceito);
                        netGames.enviarJogada(jogada);
                        netGames.finalizarPartida();
                        break;
                    case acordoNegado:
                        jogada = new JogadaMorelli(TipoJogada.acordoNegado);
                        netGames.enviarJogada(jogada);
                        break;
                    case abandonarPartida:
                        jogada = new JogadaMorelli(TipoJogada.abandonarPartida);
                        netGames.enviarJogada(jogada);
                        break;
                    case atualizarTabuleiro:
                        jogada = new JogadaMorelli(tipoJogada.atualizarTabuleiro, tabuleiroAtualizado);
                        netGames.enviarJogada(jogada);
                        break;
                    default:
                        jogada = new JogadaMorelli(tipoJogada.encerramento);
                        String msg = msgs.getString("TheMatchIsOver");
                        notificar(msg);
                        netGames.finalizarPartida();
                        break;
                }

            }
        }
    }

    public void receberJogada(JogadaMorelli jogada) {

        setDaVez(true);
        tela.informar(msgs.getString("YourTimeToPlay"));
        TipoJogada tipoJogada = jogada.getTipoDeJogada();

        if (null != tipoJogada) {
            switch (tipoJogada) {
                case realizarAcordo:
                    this.realizarAcordo();
                    break;
                case acordoAceito:
                    finalizarPartidaEmpate();
                    break;
                case acordoNegado:
                    tela.exibeMensagemAcordoNegado();
                    break;
                case abandonarPartida:
                    tabuleiro.setPartidaEmAndamento(false);
                    netGames.finalizarPartida();
                    String msg = netGames.getNomeJogador()
                    		+ " " + msgs.getString("YourTimeToPlay") 
                    		+ " " + msgs.getString("YouAreTheWinner");
                    notificar(msg);
                    break;
                case atualizarTabuleiro:
                    atualizaTabuleiro(jogada.getTabuleiro());
                    break;
                case encerramento:
                    informaPartidaEncerrada();
                    break;
                default:
                    break;
            }
        }
    }

    public void abandonarPartida() {
        if (tabuleiro.isPartidaEmAndamento()) {
            tabuleiro.setPartidaEmAndamento(false);
            enviarJogada(TipoJogada.abandonarPartida);
            JOptionPane.showMessageDialog(tela, 
            		msgs.getString("You")
            		+ " " + msgs.getString("YourTimeToPlay")
            		+ " " + msgs.getString("YouLost"));
        }
    }

    public void atualizaTabuleiro(Faixa[] tabuleiroAtualizado) {
        this.tabuleiroAtualizado = tabuleiroAtualizado;
        tabuleiro.atualizarTabuleiro(tabuleiroAtualizado);
        tela.atualizaTabuleiro(tabuleiroAtualizado);
    }

    public void informaPartidaEncerrada() {
    	
        tabuleiro.finalizaPartida();
    }

    public void notificar(String msg) {
    	
        tela.notificar(msg);
    }

    public void notificarIrregularidade() {
    	
    	tela.notificar(msgs.getString("IrregularPlay"));
    }

    /*--- Caso de uso: conectar interno*/
    public void conectar() {

        if (!conectado) {

            String ip = tela.solicitarIpServidor();
            String nomeJogador = tela.solicitarNomeJogador();

            conectado = tabuleiro.conectar(ip, nomeJogador);

            tela.informar(msgs.getString("Connected"));
            
        } else {
        	
            tela.notificar(msgs.getString("YouAreAlreadyConnected"));
        }        
    }

    public void desconectar() {

        if (conectado) {
        	
            tabuleiro.setPartidaEmAndamento(false);
            conectado = netGames.desconectar();
//            tela.notificar(msgs.getString("YouAreDisconnected"));
            
        } else {
        	
            tela.notificar(msgs.getString("YouAreAlreadyDisconnected"));
        }

        //tela.informarEstadoConexao(conectado);
    }
    
}
