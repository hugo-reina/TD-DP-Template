import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParseurClasse
{
    public static Object parse(String nomFichierInstance) throws

            IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            SecurityException, NoSuchMethodException,  IllegalArgumentException, InvocationTargetException, NoSuchFieldException

    {
        BufferedReader f = new BufferedReader(new FileReader(nomFichierInstance));
        String ligne;

        ligne = f.readLine();         // la 1ère ligne est ignorée car elle contient
        // uniquement un commentaire
        ligne = f.readLine().trim();    // la 2ème ligne contient le nom complet
        // (avec préfixe package) de la classe à instancier

        Class<?> maClasse = Class.forName(ligne); // on charge la classe à instancier.
        // La variable maClasse la représente
        Object résultat = maClasse.newInstance(); // on appelle le constructeur par défaut
        // de la classe chargée

// les lignes suivantes contiennent les valeurs pour les champs à renseigner.
// On va boucler sur ces lignes

        while ((ligne = f.readLine()) != null)
        {
            String[] éléments = ligne.split("=");
            String nomChamp = éléments[0].trim();
            String sValeur = éléments[1].trim();

            String nomSetter = créeNomSetter(nomChamp);  // on crée le nom du setter
            // associé au champ à renseigner
            if (sValeur.endsWith(".txt"))
            {
                Field champ = maClasse.getDeclaredField(nomChamp);
                Class<?> typeChamp = champ.getType();    // typeChamp représente le type
                // du champ

                Method setter = maClasse.getMethod(nomSetter, typeChamp);

                setter.invoke(résultat, ParseurClasse.parse(sValeur));  // appel récursif
                // puis conversion de Object vers la classe représentée par typeChamp
            }
            else
            {
                Method setter = maClasse.getMethod(nomSetter, java.lang.String.class);   	// on obtient la méthode setChamp(String) associée au champ à renseigner
                setter.invoke(résultat, sValeur);                                               	// on appelle le setter et on renseigne donc le champ
            }
        }
        f.close();
        return résultat;
    }	// parse
    /**
     * crée le nom du setter en fonction du nom du champ de la manière suivante :
     *
     * nomDeChamp ---> setNomDeChamp(...)
     *
     * @param nomChamp : nom du champ respectant les conventions de nommage
     * @return nomSetter respectant les conventions de nommage
     *
     * */
    public static String créeNomSetter(String nomChamp)
    {
        char l = nomChamp.charAt(0);
        String nomSetter = "set"+Character.toUpperCase(l)+nomChamp.substring(1);
// on crée le nom du setter associé au champ à renseigner
        return nomSetter;
    }

}	// ParseurClasse
