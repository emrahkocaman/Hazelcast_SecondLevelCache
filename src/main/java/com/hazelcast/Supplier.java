package com.hazelcast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Supplier
{
    private int id;
    private String name;
    private List<Product> products = new ArrayList<Product>();
    private long version;
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public List<Product> getProducts()
    {
        return products;
    }
    public void setProducts(List products)
    {
        this.products = products;
    }
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
}
