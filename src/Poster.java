package Oskroba_Miron_lab_03;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
/*
 *    Plik: Poster.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
enum PosterType {
	UNKNOWN("--"), 
	PROPAGANDA("Propagandowy"), 
	INFO("Informacyjny"), 
	ADVERTISING("Reklamowy");

	String posterType;

	private PosterType(String posterType) {
		this.posterType = posterType;
	}
	
	@Override
	public String toString() {
		return posterType;
	}
	
	public static PosterType find(String type)  {
		for(PosterType t : values()) {
			if(type.equals(t.toString())) {
				return t;
			}
		}
		return null;
	}
	
}//enum PosterType

class PosterException extends Exception {
	private static final long serialVersionUID = 1L;

	public PosterException(String message) {
		super(message);
	}
	
}//class PosterException

/*
 * 		implementacja interfejsu Serializable pozwala na zapis i odczyt danych z pliku
 * 		implementacja interfejsu Comparable pozwala na porownywanie dwoch obiektow
 * 		przeciazenie metod equals(Object) oraz hashCode pozwala na dokladne sprawdzenie czy oba obiekty sa w sobie rowne 
 * 			- pozwala to na otrzymanie poprawnego stanu logicznej otrzymanego za posrednictwem metody Collection.contains(Object)
 * 		
 */
public class Poster implements Comparable<Poster>,Serializable {
	private static final long serialVersionUID = 1L;
	private PosterType type;
	private String color;
	private int photosNumber;
	private Dimension size;
	
	public Poster(String posterType, String size) throws PosterException {
		setType(posterType);
		setSize(size);
	}
	
	public void setSize(String size) throws PosterException {
		String[] sizes = size.split("x");
		if(sizes.length!=2){
			throw new PosterException("Wymiar plakatu musi byæ w formacie liczba1xliczba2, liczba z przedzia³u [0-2000]");
		}
		
		int x,y;
		
		try {
		x = Integer.parseInt(sizes[0]);
		y = Integer.parseInt(sizes[1]);
		}catch (NumberFormatException e) {
			throw new PosterException("Wymiar plakatu musi byæ w formacie liczba1xliczba2, liczba z przedzia³u [0-2000]");
		}		
		if((x<0 || y<0) || x>2000 | y > 2000)
			throw new PosterException("Wymiary musz¹ byæ w przedziale [0-2000]");
		this.size = new Dimension(x,y);
	}
	
	public void setType(String type) throws PosterException {
		if(type == null || type.equals("")) throw new PosterException("Nale¿y okreœliæ typ plakatu.");
		PosterType foundType = PosterType.find(type);
		if(foundType == null) throw new PosterException("Podany typ plakatu nie istnieje.");
		else this.type = foundType;
	}
	
	public void setColor(String color) throws PosterException {
		if(color == null || color.equals("")) throw new PosterException("Nale¿y okreœliæ kolor plakatu.");
		this.color = color;
	}
	
	public void setPhotosNumber(int photosNumber) throws PosterException {
		if(photosNumber < 0) throw new PosterException("Nieprawid³owa liczba zdjêæ.");
		this.photosNumber = photosNumber;
	}
	
	public Dimension getDimensionSize() {
		return size;
	}
	
	public String getStringSize() {
		return String.format("%.0f", size.getWidth()) + "x" + String.format("%.0f", size.getHeight()); 
	}
	
	public PosterType getType() {
		return type;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getPhotosNumber() {
		return photosNumber;
	}
	
	@Override
	public String toString() {
		return type + "#" + getStringSize() + "#" + color + "#" + photosNumber;
	}
	
	public static void printToFile(PrintWriter writer, Poster poster){
		writer.println(poster.toString());
	}
	
	public static void printToFile(String fileName, Poster poster) throws PosterException{
		fileName+=".txt";
		try(PrintWriter writer = new PrintWriter(fileName)){
			printToFile(writer, poster);
		} catch (FileNotFoundException e) {
			throw new PosterException("Nie odaleziono pliku.");
		}
	}
	
	public static Poster readFromFile(BufferedReader reader) throws PosterException{
		try {
			String line = reader.readLine();
			if(line == null || line.equals("")) return null;
			String[] txt = line.split("#");
			Poster poster = new Poster(txt[0], txt[1]);
			
			poster.setColor(txt[2]);
			poster.setPhotosNumber(Integer.parseInt(txt[3]));	
			return poster;
		} catch(IOException e){
			throw new PosterException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}	
	}
	
	public static Poster readFromFile(String fileName) throws PosterException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
			return Poster.readFromFile(reader);
		} catch (FileNotFoundException e){
			throw new PosterException("Nie odnaleziono pliku " + fileName);
		} catch(IOException e){
			throw new PosterException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}	
	}
	
	@Override
	public int compareTo(Poster o1) {
		int dimXDiff = (int) (o1.getDimensionSize().getWidth() - this.getDimensionSize().getWidth());
		int dimYDiff = (int) (o1.getDimensionSize().getHeight() - this.getDimensionSize().getHeight());
		if(dimXDiff == 0 && dimYDiff == 0)
			return 0;
		return 1;
	}
	
//methods to help collection.contains(Object o) return proper value.	
	@Override
	public boolean equals(Object p) {
		Poster poster = (Poster)p;
		if(photosNumber == poster.photosNumber)
			if(type == poster.type)
				if(color.toLowerCase().equals(poster.color.toLowerCase()))
					if(getStringSize().equals(poster.getStringSize()))
						return true;
		return false;
		
	}
	
	@Override
	public int hashCode() {
		return 0;		
	}
	
}// class Poster
