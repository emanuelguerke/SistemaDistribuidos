package entities;

public class Ordem {
	private String autor;
	private String descricao;
	private String status;
	private int id;
	
	private Ordem[] ordens;

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Ordem[] getOrdens() {
		return ordens;
	}

	public void setOrdens(Ordem[] ordens) {
		this.ordens = ordens;
	}
	
	
	
	
}
