import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Floating {
	
	private double freq[] = new double[128];
	private double lower[]= new double[128];
	private double upper[]= new double[128];
	private double temp = 0;
	private int Length = 0;
	private double val = 0;
	private double low = 0 ;
	private double up = 1 ;
	
	
	private void reset(){
		for(int i=0;i<128;++i){
			freq[i] = 0;
			lower[i] = 0;
			upper[i] = 0;
			up = 1;
			low = 0;
			temp = 0;
			Length =0;
		}
	}
	
	
	private char get(double num){
		for(int i=0;i<128;++i)
			if(num >= lower[i] && num <= upper[i])
				return (char)i;
		return 0;
	}
	
	
	
	void compress(String filePath){
		File file = new File(filePath+".txt");
		String data = "";
		try {
			Scanner in = new Scanner(file);
			data = in.nextLine();
		} catch (FileNotFoundException e) {
			// 
			e.printStackTrace();
		}
		
		
		
		for(int i=0;i<data.length();++i)freq[data.charAt(i)]++;
		
		for(int i=0;i<128;++i)freq[i]=(double)freq[i]/data.length();
		
		

		
		for(int i=0;i<128;++i){
			if(freq[i]==0){
				lower[i] = 0;  
				upper[i] = 0;
			}
			else{
			lower[i] = temp;
			temp += freq[i];
			upper[i] = temp;
			}
		}
	
		double range;
		for(int i=0;i<data.length();++i){
			range = up-low;
			up = upper[data.charAt(i)]*range+low;
			low = lower[data.charAt(i)]*range+low;
		}
		
		
		
		File f = new File("z-" + filePath);
		try {
			PrintWriter p = new PrintWriter(f);
			p.println((up+low)/2.0);
			
			for(double i : freq)p.print(i + " ");
			p.println();
			p.println(data.length());
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	
	void decompress(String filePath){
		reset();
		String s = "" ;
		File file =  new File(filePath);
		try {
			Scanner in = new Scanner(file);
			val = in.nextDouble();
			for(int i=0;i<128;++i)freq[i] = in.nextDouble();
			Length = in.nextInt();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<128;++i){
			if(freq[i]==0){
				lower[i] = 0;  
				upper[i] = 0;
			}
			else{
			lower[i] = temp;
			temp += freq[i];
			upper[i] = temp;
			}
		}
		
	
		
		double nval = val;	
		for(int i=0;i<Length;++i){
			char c = get(nval);
			s+=c;
			up = upper[c]*(up-low)+low;
			low = lower[c]*(up-low)+low;
			nval= (val-low) / (up-low);
		}
		

		filePath+=".txt";
		
		try {
			PrintWriter p = new PrintWriter(filePath);
			p.println(s);
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
			
	}

}
