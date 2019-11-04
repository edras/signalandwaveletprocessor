package br.com.evsqp.math.vector;

import java.text.DecimalFormat;


public class MathV {
	
	public static void main(String[] args) {
		 
		double[] v = new double[]{1, 2, 1, 3, 3, 5, 1, 7, 4, 2, 5, 7, 2, 2, 2, 15 };
		
		DecimalFormat f = new DecimalFormat("0.00");
		System.out.println("Vector:         1, 2, 1, 3, 3, 5, 1, 7, 4, 2, 5, 7, 2, 2, 2, 15");
		System.out.println("Ordered Vector: 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 4, 5, 5, 7, 7, 15");
		System.out.println("Maximum: "  		+f.format(MathV.max(v)));
		System.out.println("Minimum: "  		+f.format(MathV.min(v)));
		System.out.println("Mean: " 			+f.format(MathV.mean(v)));
		System.out.println("Variance: "  		+f.format(MathV.var(v)));
		System.out.println("Std Deviation: " 	+f.format(MathV.dev(v)));
		System.out.println("Energy: "	  		+f.format(MathV.energy(v)));
		System.out.println("Average Pow: "		+f.format(MathV.avg_pow(v)));
		System.out.println("RMS: "				+f.format(MathV.rms(v)));
		System.out.println("Norm: "				+f.format(MathV.norm(v)));
		System.out.println("Median: "			+f.format(MathV.median(v)));
		System.out.println("Mode: "				+f.format(MathV.mode(v)));
		System.out.println("Range: "			+f.format(MathV.range(v)));
		System.out.println("Centered Energy: "	+f.format(MathV.centeredEnergy(v)));
		System.out.println("Approx. Entropy: "	+f.format(MathV.apEn(v)));
		
		
		System.out.println("\n\nTeste da Classe java.lang.Number:");
		double n = 2.37;
		Number number = n;
		System.out.println("Numero original como double: " + n);
		System.out.println("Number Byte:" + number.byteValue());
		System.out.println("Number Inteiro:" + number.intValue());
		System.out.println("Number Double:" + number.doubleValue());
		System.out.println("Number Float:" + number.floatValue());
		System.out.println("Number Long:" + number.longValue());
		System.out.println("Number Short:" + number.shortValue());
		System.out.println("Number String:" + number.toString());
		
	}

	public static double range(double[] v) {
		return max(v)-min(v);
	}
	
	public static double mode(double[] v) {
		
		double[] r = orderLow2High(v);
		
		double mode_ = r[0];
		double mode_tmp;
		
		int n = r.length;
		int count = 1;
		int count_tmp = 0;
		
		int i = 0;
		while(r[++i]==mode_ && i<n) count++;
		
		if(i<n) mode_tmp = r[i++];
		else return mode_;
		
		for (; i < n; i++) {
			if(r[i] == mode_tmp) count_tmp++;
			else {
				if(count_tmp > count){
					mode_ = mode_tmp;
					count = count_tmp;
				}
				mode_tmp = r[i];
				count_tmp = 1;
			}
		}
		return mode_;
	}
	
	private static double[] orderLow2High(double[] v){
		
		int n = v.length;
		double[] r = new double[n];
		System.arraycopy(v, 0, r, 0, n);
		double tmp;
		for (int i = 0; i < n; i++) {
			for (int j = i+1; j < n; j++) {
				if(r[i]>r[j]){
					tmp = r[i];
					r[i] = r[j];
					r[j] = tmp;
				}
			}
		}
		return r;
	}
	
	public static double apEn(double[] v) {
		
		int m = 2;
		double dev = 0.15*dev(v);

//		v = new double[51];
//		
//		v[0] = 85;
//		v[1] = 80;
//		v[2] = 89;
//		//v[3] = 64;
//		//v[4] = 65;
//		
//		// 51 samples { 85, 80, 89, 85, 80, 89}
//		for (int i = 3; i < v.length; i++) {
//			v[i++] = v[0];
//			v[i++] = v[1];
//			//v[i++] = v[2];
//			//v[i++] = v[3];
//			v[i]   = v[2];
//		}
//		
//		// calcula desvio padrão
//		double dev = dev(v);
//		dev = 2;
		
		int [] a = new int[v.length-m+1];
		int [] b = new int[v.length-m];
		boolean check;

		for (int i = 0; i < v.length-m+1; i++) {
			for (int j = 0; j < a.length; j++) {
				check = true; 
				for (int j2 = 0; j2 < m; j2++) {
					if(Math.abs(v[j+j2]  -v[i+j2])  >dev)
						check = false;
				}
				if(check) a[j]++;
				//if(Math.abs(v[j]  -v[i])  >dev) continue;
				//if(Math.abs(v[j+1]-v[i+1])<dev) a[j]++;
			}
			for (int k = 0; k < b.length; k++) {
				if((i+m+1)>v.length) break;
				check = true;
				for (int j2 = 0; j2 < m+1; j2++) {
					if(Math.abs(v[k+j2]  -v[i+j2])  >dev)
						check = false;
				}
				if(check) b[k]++;
				//if(Math.abs(v[k]  -v[i])  >dev) continue;
				//if(Math.abs(v[k+1]-v[i+1])>dev) continue;
				//if(Math.abs(v[k+2]-v[i+2])<dev) b[k]++;
			}			
		}
		
		double Ca = 0;
		double Cb = 0;
		for (int i = 0; i < a.length; i++) {
			Ca += (double)a[i]/(v.length-m+1);
		}
		for (int i = 0; i < b.length; i++) {
			Cb += (double)b[i]/(v.length-m);
		}
		
		Ca /= (v.length-m+1);
		Cb /= (v.length-m);
		
		return Math.log(Ca/Cb);
	}

	public static double median(double[] v) {
		double[] r = orderLow2High(v);
		int n = r.length;
		if(n%2==0) return (r[n/2-1]+r[n/2])/2;
		else return r[((n+1)/2)-1];
	}

	public static double max(double[] v){
		
		double r = v[0];
		for (int i = 1; i < v.length; i++)
			if(v[i]>r) r=v[i];
		return r;		
	}
	
	public static double min(double[] v){
		
		double r = v[0];
		for (int i = 1; i < v.length; i++)
			if(v[i]<r) r=v[i];
		return r;		
	}
	
	public static double mean(double[] v){
		
		double r = v[0];
		for (int i = 1; i < v.length; i++)
			r+=v[i];
		return r/v.length;		
	}
	
	private static double x_m2(double[] v){
		
		double m = mean(v);
		double r = 0;
		
		for (int i = 0; i < v.length; i++)
			r+=Math.pow(v[i]-m, 2);
		
		return r;
	}
	
	public static double var(double[] v){

		return x_m2(v)/(v.length-1);				
	}

	public static double dev(double[] v){
		
		return Math.sqrt(var(v));		
	}
	
	public static double energy(double[] v){
		
		double r = 0;
		for (int i = 0; i < v.length; i++)
			r+= Math.pow(v[i],2);
		return r;		
	}
	
	// considerando que o evento está centrado na amostra
	// essa função retorna a divisão da energia das amostras centrais
	// pela energia da amostra principal
	public static double centeredEnergy(double[] v){
		
		double r = energy(v);
		int l = v.length/5;
		if(l == 0) return 0;
		
		double[] c = new double[l];
		System.arraycopy(v, v.length/2-l/2, c, 0, l);
		r = energy(c)/r;
		
		return r;
	}
	
	public static double avg_pow(double[] v){
		
		return energy(v)/v.length;
	}
	
	public static double rms(double[] v){
		
		return Math.sqrt(avg_pow(v));
	}
	
	public static double norm(double[] v){
		
		return Math.sqrt(energy(v));
	}

}
