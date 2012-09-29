package com.lnu.mycountries.db;

import java.util.Comparator;

import com.lnu.db.BaseEntity;

public class Country extends BaseEntity {
	
	private static final long	serialVersionUID	= 1L;
	private int					year;
	private String				name;
	
	public Country() {
		
	}
	
	public Country(int year, String name) {
		super();
		this.year = year;
		this.name = name;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static final Comparator<Country>	COUNTRY_YEAR_COMPARATOR	= new CountryYearComparator();
	
	private static class CountryYearComparator implements Comparator<Country>, java.io.Serializable {
		private static final long	serialVersionUID	= 1L;
		
		@Override
		public int compare(Country c1, Country c2) {
			
			return Integer.valueOf(c1.year).compareTo(c2.year);
		}
		
	}
	
	public static final Comparator<Country>	COUNTRY_NAME_COMPARATOR	= new CountryNameComparator();
	
	private static class CountryNameComparator implements Comparator<Country>, java.io.Serializable {
		private static final long	serialVersionUID	= 1L;
		
		@Override
		public int compare(Country c1, Country c2) {
			
			return c1.name.compareTo(c2.name);
		}
	}
	
	@Override
	public String toString() {
		return year + " " + name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + year;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	
}
