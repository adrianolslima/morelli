package partesInterface;

import java.util.Locale;
import java.util.ResourceBundle;

import cip.ComponentInterface;
import estrutura.Controlador;

public class LogicaMorelli extends ComponentInterface {

	@Override
	public void initialize() {

		PortInterface pInterface = new PortInterface("logicaInterface");
		pInterface.initialize();
		ports.add(pInterface);
		
		PortProxy pProxy = new PortProxy("logicaProxy");
		pProxy.initialize();
		ports.add(pProxy);
		
		Locale currentLocale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"ApplicationMessages", currentLocale);
		
		Controlador tabuleiro = new Controlador(bundle);
		
		pInterface.setReferenciaInterna(tabuleiro);
		pProxy.setReferenciaInterna(tabuleiro);
		
		tabuleiro.setPortoInterface(pInterface);
		tabuleiro.setPortoProxy(pProxy);
	}

}
