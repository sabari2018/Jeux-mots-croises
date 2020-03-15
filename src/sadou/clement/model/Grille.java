package sadou.clement.model;

/**
 * Classe Grille générique qui permet de representer une grille
 * @param <T> Type générique
 */
public class Grille<T> {
	private int hauteur, largeur ;
	protected Object[][] cellules ;

	@SuppressWarnings("unchecked")
	public Grille(int hauteur, int largeur) {
		assert hauteur >= 0;
		assert largeur >= 0;
		this.hauteur = hauteur ;
		this.largeur = largeur ;
		cellules = (T[][]) new Object[hauteur][largeur] ;
	}

	public int getHauteur(){ return hauteur; }

	public int getLargeur(){ return largeur; }

	public boolean coordCorrectes(int lig, int col){
		return 1<=lig && lig <=getHauteur() && 1<=col && col <=getLargeur() ;
	}

	@SuppressWarnings("unchecked")
	public T getCellule(int lig, int col){
		assert coordCorrectes(lig, col) ;
		return (T) cellules[lig-1][col-1] ;
	}

	public void setCellule(int lig, int col, T ch){
		assert coordCorrectes(lig, col) ;
		cellules[lig-1][col-1] = ch ;
	}

	@Override
	public String toString(){
		StringBuffer resultat = new StringBuffer() ;
		for (int l=1; l<=this.getHauteur(); l++)
		{
			for (int c=1; c<=this.getLargeur(); c++)
			{
				if(c>1) { resultat.append('|') ; }
				resultat.append(this.getCellule(l, c)) ;
			}
			resultat.append('\n') ;
		}
		return resultat.toString() ;
	}
}
