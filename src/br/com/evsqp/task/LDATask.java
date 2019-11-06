package br.com.evsqp.task;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import br.com.evsqp.main.EVApp;
import br.com.evsqp.view.LogWindowForm;
import br.com.evsqp.lda.LDA;

public class LDATask extends EVTask {

	EVApp controller;
	LDA lda;
	int testTrial;
	double ldaPossibilities;
	double roc = 0;
	double max_roc = -1;
	double roc_trial = -1;
	BigInteger trial;
	NumberFormat formatter;
	
	public LDATask(EVApp controller) {
		super(controller);
		this.controller = controller;
		name = "LDA Calculator";
		lda = controller.getLda();
	}

	private boolean startThread() {
		
		String tmp = controller.getView().getTestField().getText();
		testTrial = -1;
		if(tmp != null){					
			if(!tmp.isEmpty()){
				try{
					testTrial = (int)Double.parseDouble(tmp);
					double calcLdaPossibilities = controller.calcLdaPossibilities();
					if(testTrial<0 || testTrial>=calcLdaPossibilities){
						JOptionPane.showMessageDialog(null, "Número de teste deve ser um número inteiro entre 0 e "+(calcLdaPossibilities-1));
						return false;
					}
				}
				catch (Exception e2) { 
					JOptionPane.showMessageDialog(null, "Número de teste inválido");
					return false;
				}
			}
		}
		
		// calcula total de possibilidades
		ldaPossibilities = controller.calcLdaPossibilities();
		formatter = new DecimalFormat("0.0000");
        
		trial = BigInteger.ZERO;
        
        if(testTrial!=-1){
        	int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Testar somente o teste " + testTrial + "?", "", JOptionPane.YES_NO_OPTION);
        	if(showConfirmDialog == JOptionPane.YES_OPTION) ldaPossibilities = testTrial+1;
        	trial = new BigInteger(String.valueOf(testTrial));
        } else {
        	testTrial = 0;
        	int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Deseja ver resultado de todas as combinações?");
        	if(showConfirmDialog == JOptionPane.NO_OPTION) lda.setEcho(false);
        	else if(showConfirmDialog ==  JOptionPane.YES_OPTION) lda.setEcho(true);
        	else return false;
        }
        
		// filtra somente os campos a serem utilizados
		lda.filterClasses(controller.getLDAFilter());
		
		// salva matrizes filtradas
		lda.bckpClasses();
		
		return true;
		
	}

	@Override
	protected Object doInBackground() throws Exception {
		
		if(!startThread()) return null;
		
		setMessage(name + " processing...");
			
		// inicia janela de LOG
        LogWindowForm log = new LogWindowForm();
        log.setTitle("Testando possibilidades:");
        long start = System.currentTimeMillis();
        log.appendText("Hora de início: " + (new Date(start)));
        log.setVisible(true);
		
		for (double i = testTrial; i < ldaPossibilities; i++) {
			lda.calcLdaProjTrial(trial);
			roc = lda.calcROC();
			if(roc>=max_roc){
				max_roc = roc;
				roc_trial = i;
				if(!lda.isEcho()){
					log.appendText("\n" + i + ": AUC = " + formatter.format(roc));
					log.repaint();
				}
					
			}
			trial = trial.add(BigInteger.ONE);
			if(lda.isEcho()){
				log.appendText("\n" + i + ": AUC = " + formatter.format(roc));
				log.repaint();
			}
			int teste = (int)((i/ldaPossibilities)*100);
			setProgress(teste);
			if(pause) loop_pause();
		}	
		
		long stop = System.currentTimeMillis();
		log.appendText("\nHora de término: " + (new Date(stop)));
		
		long total = stop-start;
		
		int ms      = (int) (total % 1000);
		int seconds = (int) (total / 1000) % 60 ;
		int minutes = (int) ((total / (1000*60)) % 60);
		int hours   = (int) ((total / (1000*60*60)) % 24);
		int days    = (int) (total / (1000*60*60*24));
		
		log.appendText("\nTempo total: " + days + "d "+hours+":"+minutes+":"+seconds+"."+ms);
		log.appendText("\nMelhor ROC: " + formatter.format(max_roc) + " test: " + roc_trial);			
		
		return null;
	}

	@Override
	protected void cancelled() {
		setMessage(name + " canceled!");
		controller.getMonitor().setLdaTask(false);
		super.cancelled();
	}

	@Override
	protected void succeeded(Object result) {
		setMessage(name + " finished!");
		controller.getMonitor().setLdaTask(false);
		super.succeeded(result);
	}

}
