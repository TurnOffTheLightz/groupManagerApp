package Oskroba_Miron_lab_03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
/*
 *    Plik: GroupOfPosters.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
enum GroupType{
	VECTOR("Lista   (klasa Vector)"),
	ARRAY_LIST("Lista   (klasa ArrayList)"),
	LINKED_LIST("Lista   (klasa LinkedList)"),
	HASH_SET("Zbiór   (klasa HashSet)"),
	TREE_SET("Zbiór   (klasa TreeSet)");
	
	String groupType;
	
	private GroupType(String type) {
		this.groupType = type;
	}
	
	@Override
	public String toString() {
		return groupType;
	}
	
	public static GroupType find(String type) {
		for(GroupType gType : values()) {
			if(type.toLowerCase().equals(gType.toString().toLowerCase()))
				return gType;
		}
		return null;
	}
	
	public Collection<Poster> createCollection() throws PosterException{
		switch(this) {
		case VECTOR:
			return new Vector<Poster>();
		case ARRAY_LIST:
			return new ArrayList<Poster>();
		case LINKED_LIST:
			return new LinkedList<Poster>();
		case HASH_SET:
			return new HashSet<Poster>();
		case TREE_SET:
			return new TreeSet<Poster>();
		default:
			throw new PosterException("Nieprawid³owy typ kolekcji.");
		}
	}
}//enum GroupType


/*
 * 		implementacja interfejsu Iterable pozwala na przegladniecie wszystkich elementow danej kolekcji
 * 
 */
public class GroupOfPosters implements Serializable,Iterable<Poster>{ 
	private static final long serialVersionUID = 1L;

	private String groupName;
	private GroupType groupType;
	private Collection<Poster> collection;

	public GroupOfPosters(String gType, String gName) throws PosterException {
		setGroupType(gType);
		if(groupType == null) throw new PosterException("Nieprawid³owy typ kolekcji.");
		setGroupName(gName);
		collection = groupType.createCollection();
	}
	
	private void setGroupType(String gType) throws PosterException {
		if(gType == null || gType.equals("")) throw new PosterException("Nale¿y okreœliæ typ kolekcji.");
		this.groupType = GroupType.find(gType);
		if(groupType == null) throw new PosterException("Nale¿y okreœliæ typ kolekcji.");
	}
	
	public void setGroupName(String gName) throws PosterException {
		if(gName == null || gName.equals("")) throw new PosterException("Nale¿y okreœliæ nazwê grupy.");
		this.groupName = gName;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public GroupType getGroupType() {
		return groupType;
	}
	
	public void changeGroupType(GroupType gType) throws PosterException {
		if(gType == null) throw new PosterException("Nale¿y okreœliæ typ grupy.");
		if(gType == groupType) return;
		
		this.groupType = gType;
		Collection<Poster> lastCollection = collection;
		this.collection = groupType.createCollection();
		
		for(Poster poster : lastCollection)
			collection.add(poster);
	}
	
	public boolean add(Poster p) {
		return collection.add(p);
	}
	
	public int size() {
		return collection.size();
	}
	
	@Override
	public Iterator<Poster> iterator() { 
		return collection.iterator();
	}

	//TODO::
	public void sortPosterType() throws PosterException{
		if(groupType == GroupType.HASH_SET || groupType == GroupType.TREE_SET)
			throw new PosterException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		
		Collections.sort((List<Poster>) collection, new Comparator<Poster>() {
				@Override
				public int compare(Poster o1, Poster o2) {
					String type1 = o1.getType().toString();
					String type2 = o2.getType().toString();
					if(type1 == type2)
						return 0;
					if(type1 == null)
						return -1;
					if(type2 == null)
						return 1;
					return type1.compareTo(type2);
				}
			});		
	}
	
	public void sortSize() throws PosterException{
		if(groupType == GroupType.HASH_SET || groupType == GroupType.TREE_SET)
			throw new PosterException("Kolekcje typu SET nie mog¹ byæ sortowane.");

		Collections.sort((List<Poster>)collection);//Interfejs Comparable w klasie Poster domyœlnie porównuje wyimary plakatów.
	}
	
	public void sortPhotosNumber() throws PosterException {
		if(groupType == GroupType.HASH_SET || groupType == GroupType.TREE_SET)
			throw new PosterException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		Collections.sort((List<Poster>) collection, new Comparator<Poster>() {

			@Override
			public int compare(Poster o1, Poster o2) {
				return o1.getPhotosNumber() - o2.getPhotosNumber();
			}
		});	
	}
	
	public void sortColor() throws PosterException {
		if(groupType == GroupType.HASH_SET || groupType == GroupType.TREE_SET)
			throw new PosterException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		Collections.sort((List<Poster>) collection, new Comparator<Poster>() {

			@Override
			public int compare(Poster o1, Poster o2) {
				String color1 = o1.getColor();
				String color2 = o2.getColor();
				if(color1 == color2)
					return 0;
				if(color1 == null)
					return -1;
				if(color2 == null)
					return 1;
				return color1.compareTo(color2);
			}
		});	
	}
	
	@Override
	public String toString() {
		return "groupName: "+ groupName + ", groupType: " + groupType;
	}
	
	public static void printToFile(PrintWriter printWriter, GroupOfPosters group) {
		printWriter.println(group.getGroupName());
		printWriter.println(group.getGroupType());
		for(Poster poster : group)
			Poster.printToFile(printWriter, poster);
	}
	
	public static void printToFile(String fileName, GroupOfPosters group) throws PosterException {
		fileName+=".txt";
		try(PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName)))){
			printToFile(writer, group);
		} catch (IOException e) {
			throw new PosterException("Nie znaleziono pliku.");
		}
	}
	
	public static GroupOfPosters readFromFile(BufferedReader reader) throws PosterException {
		try {
			String gName = reader.readLine();
			String gType= reader.readLine();
			GroupOfPosters groupOfPosters = new GroupOfPosters(gType, gName);
			
			Poster poster;
			while((poster = Poster.readFromFile(reader)) != null)
				groupOfPosters.add(poster);
			return groupOfPosters;
		} catch (IOException e) {
			throw new PosterException("B³¹d odczytu danych z pliku.");
		}
	}
	
	public static GroupOfPosters readFromFile(String fileName) throws PosterException {
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
			return GroupOfPosters.readFromFile(reader);
		} catch (FileNotFoundException e) {
			throw new PosterException("Nie odnaleziono pliku " + fileName);
		} catch (IOException e) {
			throw new PosterException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}
	
//OPERATIONS	
	public static GroupOfPosters createGroupSum(GroupOfPosters g1, GroupOfPosters g2) throws PosterException {
		String gName = "(" + g1.groupName + " OR " + g2.groupName + ")";
		GroupType gType;
		if(g1.collection instanceof Set && !(g2.collection instanceof Set))
			gType = g2.groupType;
		else
			gType = g1.groupType;
		
		GroupOfPosters result = new GroupOfPosters(gType.toString(), gName);
		//
		result.collection.addAll(g1.collection);//g1
		result.collection.addAll(g2.collection);//g1+g2
		//
		return result;
	}
	
	public static GroupOfPosters createGroupIntersection(GroupOfPosters g1, GroupOfPosters g2) throws PosterException {
		String gName = "(" + g1.groupName + " AND " + g2.groupName + ")";
		GroupType gType;
		if(g1.collection instanceof Set && !(g2.collection instanceof Set))
			gType = g2.groupType;
		else
			gType = g1.groupType;
		
		GroupOfPosters result = new GroupOfPosters(gType.toString(), gName);

		//
		result.collection.addAll(g1.collection);//g1
		result.collection.retainAll(g2.collection);//g1 * g2
		//
		return result;
	}

	public static GroupOfPosters createGroupDifference(GroupOfPosters g1, GroupOfPosters g2) throws PosterException {
		String gName = "(" + g1.groupName + " SUB " + g2.groupName + ")";
		GroupType gType;
		if(g1.collection instanceof Set && !(g2.collection instanceof Set))
			gType = g2.groupType;
		else
			gType = g1.groupType;

		//
		GroupOfPosters result = new GroupOfPosters(gType.toString(), gName);
		result.collection.addAll(g1.collection);//g1
		result.collection.removeAll(g2.collection);//g1-g2
		
		//
		return result;
	}
	
	@SuppressWarnings("null")
	public static GroupOfPosters createGroupSymmetricDifference(GroupOfPosters g1, GroupOfPosters g2) throws PosterException {
		String gName = "(" + g1.groupName + " XOR " + g2.groupName + ")";
		GroupType gType;
		if(g1.collection instanceof Set && !(g2.collection instanceof Set))
			gType = g2.groupType;
		else
			gType = g1.groupType;

		//
		GroupOfPosters result = new GroupOfPosters(gType.toString(), gName);
		GroupOfPosters g2Copy = new GroupOfPosters(gType.toString(), gName);
		g2Copy.collection.addAll(g2.collection);
		result.collection.addAll(g1.collection);//g1
		
		result.collection.removeAll(g2Copy.collection);//g1-g2
		g2Copy.collection.removeAll(g1.collection);//g2-g1

		
		result.collection.addAll(g2Copy.collection);//(g1-g2) + (g2-g1)
		//
		return result;
	}
}
