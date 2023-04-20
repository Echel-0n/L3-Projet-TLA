package application;

/*
Enumération des états possibles d'un carreau
*/
enum EtatCarreau {
    VIDE,               // carreau vide, le joueur peut se rendre dessus
    MUR,                // bloque le passage
    SORTIE,             // le joueur gagne s'il se rend dessus
    COMMUTATEUR_OFF,    // levier commutateur tournée à gauche, le joueur peut se rendre dessus
    COMMUTATEUR_ON,     // levier commutateur tournée à droite, le joueur peut se rendre dessus
    PORTE_FERMEE,       // bloque le passage
    ENTREE_TRAPPE_HAUT,		// Place l'entrée d'un trappe en provenance du haut
    ENTREE_TRAPPE_DROITE,		// Place l'entrée d'un trappe en provenance de la droite
    ENTREE_TRAPPE_BAS,		// Place l'entrée d'un trappe en provenance du bas
    ENTREE_TRAPPE_GAUCHE,		// Place l'entrée d'un trappe en provenance de la gauche
    SORTIE_TRAPPE		// Place la sortie d'une trappe
};
