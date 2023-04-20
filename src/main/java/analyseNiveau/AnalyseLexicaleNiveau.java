package analyseNiveau;

import java.util.ArrayList;
import java.util.List;

import analyseNiveau.exceptions.LexicalErrorException;

/**
 * Classe qui analyse le <b>lexique</b> d'un niveau par la méthode {@link analyseNiveau.AnalyseLexicaleNiveau.analyse analyse(String ent)}
 */
public abstract class AnalyseLexicaleNiveau {
	/**
	 * Table de transitions du langage
	 */
	private static Integer TRANSITIONS[][] = {
	//            esp  rLig ,    ;    (    )    /    A|a  B|b  C|c  D|d  E|e  F|f  G|g  H|h  I|i  J|j  K|k  L|l  M|m  N|n  O|o  P|p  Q|q  R|r  S|s  T|t  U|u  V|v  W|w  X|x  Y|y  Z|z  chif autr "
	/* 0   */  {  0   ,0   ,1001,1002,1003,1004,1   ,3   ,-1  ,8   ,18  ,24  ,32  ,36  ,-1  ,47  ,-1  ,-1  ,57  ,-1  ,67  ,72  ,80  ,-1  ,88  ,98  ,113 ,116 ,118 ,125 ,132 ,133 ,-1  ,134 ,-1  ,135  },
	/* 1   */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,2   ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 2   */  {  2   ,0   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2   ,2    },
	/* 3   */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,4   ,-1  ,6   ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 4   */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,5   ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 5   */  {  1005,1005,1005,1005,1005,1005,1005,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1005 },
	/* 6   */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,7   ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 7   */  {  1006,1006,1006,1006,1006,1006,1006,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1006 },
	/* 8   */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,9   ,-1  ,-1  ,-1  ,-1  ,-1  ,13  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 9   */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,10  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 10  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,11  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 11  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,12  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 12  */  {  1007,1007,1007,1007,1007,1007,1007,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1007 },
	/* 13  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,14  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 14  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,15  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 15  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,16  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 16  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,17  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 17  */  {  1008,1008,1008,1008,1008,1008,1008,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1008 },
	/* 18  */  {  1009,1009,1009,1009,1009,1009,1009,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,19  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1009 },
	/* 19  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,22  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,20  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 20  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,21  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 21  */  {  1009,1009,1009,1009,1009,1009,1009,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1009 },
	/* 22  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,23  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 23  */  {  1010,1010,1010,1010,1010,1010,1010,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1010 },
	/* 24  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,25  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 25  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,26  ,-1  ,-1  ,-1  ,-1  ,-1  ,30  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 26  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,27  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 27  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,28  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 28  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,29  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 29  */  {  1011,1011,1011,1011,1011,1011,1011,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1011 },
	/* 30  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,31  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 31  */  {  1012,1012,1012,1012,1012,1012,1012,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1012 },
	/* 32  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,33  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 33  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,34  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 34  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,35  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 35  */  {  1013,1013,1013,1013,1013,1013,1013,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1013 },
	/* 36  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,37  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,41  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 37  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,38  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 38  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,39  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 39  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,40  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 40  */  {  1014,1014,1014,1014,1014,1014,1014,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1014 },
	/* 41  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,42  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,45  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 42  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,43  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 43  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,44  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 44  */  {  1015,1015,1015,1015,1015,1015,1015,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1015 },
	/* 45  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,46  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 46  */  {  1016,1016,1016,1016,1016,1016,1016,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1016 },
	/* 47  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,48  ,-1  ,-1  ,-1  ,-1  ,56  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 48  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,49  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 49  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,50  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 50  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,51  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 51  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,52  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 52  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,53  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 53  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,54  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 54  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,55  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 55  */  {  1017,1017,1017,1017,1017,1017,1017,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1017 },
	/* 56  */  {  1018,1018,1018,1018,1018,1018,1018,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1018 },
	/* 57  */  {  1019,1019,1019,1019,1019,1019,1019,-1  ,-1  ,-1  ,-1  ,58  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,64  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1019 },
	/* 58  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,59  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,61  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 59  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,60  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 60  */  {  1019,1019,1019,1019,1019,1019,1019,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1019 },
	/* 61  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,62  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 62  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,63  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 63  */  {  1020,1020,1020,1020,1020,1020,1020,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1020 },
	/* 64  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,65  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 65  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,66  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 66  */  {  1021,1021,1021,1021,1021,1021,1021,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1021 },
	/* 67  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,68  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 68  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,69  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 69  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,70  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 70  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,71  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 71  */  {  1022,1022,1022,1022,1022,1022,1022,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1022 },
	/* 72  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,73  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,75  ,-1  ,76  ,-1  ,79  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 73  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,74  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 74  */  {  1023,1023,1023,1023,1023,1023,1023,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1023 },
	/* 75  */  {  1024,1024,1024,1024,1024,1024,1024,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1024 },
	/* 76  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,77  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 77  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,78  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 78  */  {  1025,1025,1025,1025,1025,1025,1025,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1025 },
	/* 79  */  {  1026,1026,1026,1026,1026,1026,1026,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1026 },
	/* 80  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,81  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 81  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,82  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 82  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,83  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,85  ,-1  ,-1  ,-1  ,-1   },
	/* 83  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,84  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 84  */  {  1027,1027,1027,1027,1027,1027,1027,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1027 },
	/* 85  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,86  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 86  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,87  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 87  */  {  1028,1028,1028,1028,1028,1028,1028,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1028 },
	/* 88  */  {  1029,1029,1029,1029,1029,1029,1029,-1  ,-1  ,-1  ,-1  ,93  ,-1  ,-1  ,-1  ,89  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1029 },
	/* 89  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,90  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 90  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,91  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 91  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,92  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 92  */  {  1029,1029,1029,1029,1029,1029,1029,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1029 },
	/* 93  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,94  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 94  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,95  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 95  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,96  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 96  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,97  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 97  */  {  1030,1030,1030,1030,1030,1030,1030,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1030 },
	/* 98  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,99  ,-1  ,-1  ,-1  ,101 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,104 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,108 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 99  */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,100 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 100 */  {  1031,1031,1031,1031,1031,1031,1031,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1031 },
	/* 101 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,102 ,-1  ,-1  ,-1   },
	/* 102 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,103 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 103 */  {  1032,1032,1032,1032,1032,1032,1032,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1032 },
	/* 104 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,105 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 105 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,106 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 106 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,107 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 107 */  {  1033,1033,1033,1033,1033,1033,1033,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1033 },
	/* 108 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,109 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 109 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,110 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 110 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,111 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 111 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,112 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 112 */  {  1034,1034,1034,1034,1034,1034,1034,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1034,-1  ,1034 },
	/* 113 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,114 ,115 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 114 */  {  1035,1035,1035,1035,1035,1035,1035,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1035 },
	/* 115 */  {  1036,1036,1036,1036,1036,1036,1036,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1036 },
	/* 116 */  {  1037,1037,1037,1037,1037,1037,1037,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,117 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1037 },
	/* 117 */  {  1037,1037,1037,1037,1037,1037,1037,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1037 },
	/* 118 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,119 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 119 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,120 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 120 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,121 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 121 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,122 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 122 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,123 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 123 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,124 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 124 */  {  1038,1038,1038,1038,1038,1038,1038,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1038 },
	/* 125 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,126 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,129 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 126 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,127 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 127 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,128 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 128 */  {  1039,1039,1039,1039,1039,1039,1039,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1039 },
	/* 129 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,130 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 130 */  {  -1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,131 ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1   },
	/* 131 */  {  1040,1040,1040,1040,1040,1040,1040,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1040 },
	/* 132 */  {  1041,1041,1041,1041,1041,1041,1041,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1041,-1  ,1041,-1  ,1041 },
	/* 133 */  {  1042,1042,1042,1042,1042,1042,1042,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1042,-1  ,-1  ,1042,-1  ,1042 },
	/* 134 */  {  1043,1043,1043,1043,1043,1043,1043,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,-1  ,1043,1043,-1  ,134 ,-1  ,1043 },
	/* 135 */  {  135 ,-1  ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,135 ,1044 }
	};
	
	/**
	 * Contenu du fichier niveau
	 */
	private static String entree;
	/**
	 * Position de l'analyse dans {@link analyseNiveau.AnalyseLexicaleNiveau.entree entree}.
	 */
	private static int pos;
	
	/**
	 * Etat initial pour la {@link analyseNiveau.AnalyseLexicaleNiveau.pos pos}
	 */
	private static int ETAT_INITIAL = 0;
	
	/**
	 * Méthode principale - Analyse le niveau donné pour en sortir les Tokens pour la syntaxe
	 * <br>&nbsp&nbsp Lit chaque caractère et vérifie sa bonne position par la {@link analyseNiveau.AnalyseLexicaleNiveau.TRANSITIONS Table de transition}
	 * @param ent Contenu du fichier niveau
	 * @return Liste de Token : Différents Token du niveau dans l'ordre
	 * @throws LexicalErrorException Si erreur dnas l'analyse (mot incorrect par exemple)
	 */
	public static List<Token> analyse(String ent) throws  LexicalErrorException {
		int etat = ETAT_INITIAL;
		entree = ent;
		pos = 0;
		boolean motFini = false;
		List<Token> res = new ArrayList<Token>();
		
		String buffer = "";
		Character c = null;
		do {
			motFini = false;
			
			c = lireCaractere();
			
			
			int ind = indiceSymbole(c);
			
			etat = TRANSITIONS[etat][ind];
			
			if (etat == -1) {
				throw new LexicalErrorException("Mot non-reconnu",ent, pos);
			}
			
			if (etat == 134 | (etat == 135 & c != '"')) { // Chiffres et String
				buffer = buffer + c;
			}
			
			switch(etat) {
				case 1001:
					res.add(new Token(TypeDeToken.comma, pos)); etat = 0; motFini = true; break;
				case 1002:
					res.add(new Token(TypeDeToken.semicolon, pos)); etat = 0; motFini = true; break;
				case 1003:
					res.add(new Token(TypeDeToken.lPar, pos)); etat = 0; motFini = true; break;
				case 1004:
					res.add(new Token(TypeDeToken.rPar, pos)); etat = 0; motFini = true; break;
				case 1005:
					res.add(new Token(TypeDeToken.all, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1006:
					res.add(new Token(TypeDeToken.and, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1007:
					res.add(new Token(TypeDeToken.close, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1008:
					res.add(new Token(TypeDeToken.create, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1009:
					res.add(new Token(TypeDeToken.down, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1010:
					res.add(new Token(TypeDeToken.door, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1011:
					res.add(new Token(TypeDeToken.except, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1012:
					res.add(new Token(TypeDeToken.exit, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1013:
					res.add(new Token(TypeDeToken.from, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1014:
					res.add(new Token(TypeDeToken.ghost, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1015:
					res.add(new Token(TypeDeToken.going, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1016:
					res.add(new Token(TypeDeToken.goto_, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1017:
					res.add(new Token(TypeDeToken.invisible, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1018:
					res.add(new Token(TypeDeToken.is, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1019:
					res.add(new Token(TypeDeToken.left, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1020:
					res.add(new Token(TypeDeToken.level, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1021:
					res.add(new Token(TypeDeToken.loop, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1022:
					res.add(new Token(TypeDeToken.named, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1023:
					res.add(new Token(TypeDeToken.off, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1024:
					res.add(new Token(TypeDeToken.on, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1025:
					res.add(new Token(TypeDeToken.open, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1026:
					res.add(new Token(TypeDeToken.or, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1027:
					res.add(new Token(TypeDeToken.place, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1028:
					res.add(new Token(TypeDeToken.player, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1029:
					res.add(new Token(TypeDeToken.right, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1030:
					res.add(new Token(TypeDeToken.repeat, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1031:
					res.add(new Token(TypeDeToken.set, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1032:
					res.add(new Token(TypeDeToken.size, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1033:
					res.add(new Token(TypeDeToken.spawn, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1034:
					res.add(new Token(TypeDeToken.switch_, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1035:
					res.add(new Token(TypeDeToken.to, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1036:
					res.add(new Token(TypeDeToken.tp, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1037:
					res.add(new Token(TypeDeToken.up, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1038:
					res.add(new Token(TypeDeToken.visible, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1039:
					res.add(new Token(TypeDeToken.wall, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1040:
					res.add(new Token(TypeDeToken.when, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1041:
					res.add(new Token(TypeDeToken.x, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1042:
					res.add(new Token(TypeDeToken.y, pos)); etat = 0; motFini = true; retourArriere(); break;
				case 1043:
					res.add(new Token(TypeDeToken.num, pos, buffer)); etat = 0; motFini = true; retourArriere(); buffer = ""; break;
				case 1044:
					res.add(new Token(TypeDeToken.str, pos, buffer)); etat = 0; motFini = true; buffer = ""; break;
				default: break;
			}
			
		} while ((!motFini && etat != 0) | pos < ent.length());
		System.out.println(res);// TODO
		return res;
	}
	
	/**
	 * Lis le caractère à la {@link analyseNiveau.AnalyseLexicaleNiveau.pos position}, puis incrémente cette-dernière
	 * @return Character : caractère de la position {@link analyseNiveau.AnalyseLexicaleNiveau.pos pos}
	 */
	private static Character lireCaractere() {
		Character c;
		try {
			c = entree.charAt(pos);
			pos++;
			return c;
		} catch (StringIndexOutOfBoundsException s) {
			return null;
		}
	}
	
	/**
	 * Décrémente la {@link analyseNiveau.AnalyseLexicaleNiveau.pos position} de 1 <br>
	 * Utiliser principalement pour les états finaux
	 */
	private static void retourArriere() {
		pos = pos - 1;
	}
	
	/**
	 * Renvoie l'indice du symbole donner en argument, <br>
	 * l'indice est la colonne dans la {@link analyseNiveau.AnalyseLexicaleNiveau.TRANSITIONS table de transitions}
	 * @param c : {@link Character} pour lequel on souhaite obtenir l'indice  
	 * @return int : Indice, soit une colonne dans la {@link analyseNiveau.AnalyseLexicaleNiveau.TRANSITIONS table de transitions}
	 */
	private static int indiceSymbole(Character c) {
		if (c == null) {
			return 0;
		}
		switch(c) {
			case '\n': return 1;
			case ',': return 2;
			case ';': return 3;
			case '(': return 4;
			case ')': return 5;
			case '/': return 6;
			case 'a': return 7;
			case 'A': return 7;
			case 'b': return 8;
			case 'B': return 8;
			case 'c': return 9;
			case 'C': return 9;
			case 'd': return 10;
			case 'D': return 10;
			case 'e': return 11;
			case 'E': return 11;
			case 'f': return 12;
			case 'F': return 12;
			case 'g': return 13;
			case 'G': return 13;
			case 'h': return 14;
			case 'H': return 14;
			case 'i': return 15;
			case 'I': return 15;
			case 'j': return 16;
			case 'J': return 16;
			case 'k': return 17;
			case 'K': return 17;
			case 'l': return 18;
			case 'L': return 18;
			case 'm': return 19;
			case 'M': return 19;
			case 'n': return 20;
			case 'N': return 20;
			case 'o': return 21;
			case 'O': return 21;
			case 'p': return 22;
			case 'P': return 22;
			case 'q': return 23;
			case 'Q': return 23;
			case 'r': return 24;
			case 'R': return 24;
			case 's': return 25;
			case 'S': return 25;
			case 't': return 26;
			case 'T': return 26;
			case 'u': return 27;
			case 'U': return 27;
			case 'v': return 28;
			case 'V': return 28;
			case 'w': return 29;
			case 'W': return 29;
			case 'x': return 30;
			case 'X': return 30;
			case 'y': return 31;
			case 'Y': return 31;
			case 'z': return 32;
			case 'Z': return 32;
			case '"': return 35;
			default:
				if (Character.isWhitespace(c)) {
					return 0;
				} else if (Character.isDigit(c)) {
					return 33;
				} else {
					return 34;
				}
		}
	}
}
