package sadou.clement.model;

import javafx.beans.property.StringProperty;

public interface SpecifMotsCroises
{
	// Nombre de rangées
	public int getHauteur();

	// Nombre de colonnes
	public int getLargeur();

	// Accesseurs aux cases noires
	public boolean estCaseNoire(int lig, int col);
	public void setCaseNoire(int lig, int col, boolean noire);

	// Accesseurs à la grille de solution
	public char getSolution(int lig, int col);
	public void setSolution(int lig, int col, char sol);

	// Accesseurs à la grille du joueur
	public char getProposition(int lig, int col);
	public void setProposition(int lig, int col, StringProperty prop);

	// Accesseurs aux définitions.
	// Le paramètre "horiz" est "true" pour les définitions
	// horizontales, "false" pour les définitions verticales?
	public String getDefinition
		(int lig, int col, boolean horiz);
	public void setDefinition
		(int lig, int col, boolean horiz, String def);
}
