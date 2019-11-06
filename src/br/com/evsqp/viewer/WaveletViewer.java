package br.com.evsqp.viewer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;

import br.com.evsqp.display.wavelet.WaveletPlot;
import br.com.evsqp.reader.WaveletReader;

public class WaveletViewer extends AbstractViewer {

    /**
     *
     */
    private static final long serialVersionUID = 6795855480555628181L;
    private WaveletReader reader;

    public WaveletViewer(WaveletReader waveletReader) {
        reader = waveletReader;
    }

    @Override
    public void view() {

        double[] scale = reader.getScales();
        double[] coeff = reader.getCoeffs();

        coeff = invert(coeff);
        scale = invert(scale);

        WaveletPlot waveletPlot = new WaveletPlot(reader.getWaveletName(), coeff, scale);
        waveletPlot.pack();
        RefineryUtilities.centerFrameOnScreen(waveletPlot);
        waveletPlot.setVisible(true);

    }

    private double[] invert(double[] v) {

        double[] r = new double[v.length];
        for (int i = 0, n = v.length; i < n; i++) {
            r[i] = v[n - 1 - i];
        }
        return r;
    }

    @Override
    public void setOffset(int offset) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPageSize(int pageSize) {
        // TODO Auto-generated method stub

    }

    @Override
    protected JFreeChart createChart() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void nextPage() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void previousPage() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void incScale() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void decScale() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void goToEnd() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void goHome() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void go10Pages() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateRangeAxis() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void printPageRange() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void addPopUpMenu(ChartPanel charPanel) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getDefaultPageSize() {

        return 0; // não é usado para essa classe.
    }
}
