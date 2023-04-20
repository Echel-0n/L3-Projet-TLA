package application;

import java.util.ArrayList;
import java.util.List;

import analyseNiveau.exceptions.ConditionAnalyseErrorException;

public class Condition {
	
	public enum TypeCas {
		on,
		off
	}
	
	public static List<Condition> conditionRepository = new ArrayList<>();
	
	private static int globalId = 0;
	private int id;
	private TypeCas is;
	private List<TypeCondition> table ;
	
	public Condition() {
		table = new ArrayList<TypeCondition>();
		this.is = null;
		this.id = globalId;
		globalId++;
		conditionRepository.add(this);
	}
	public Condition(TypeCas tc) {
		table = new ArrayList<TypeCondition>();
		this.is = tc;
		this.id = globalId;
		globalId++;
		conditionRepository.add(this);
	}
	
	// Not Statics Methods
	public void add(TypeCondition tc) {
		table.add(tc);
	}
	
	public void add(Condition c) {
		for (TypeCondition tc : c.getTable()) {
			table.add(tc);
		}
	}
	
	public void setIs(TypeCas b) {
		this.is = b;
		for (TypeCondition tc : table) {
			if (tc.type == TypeDeCondition.cond) {
				search(tc.getNum()).setIs(b);
			}
		}
	}
	public boolean analyse(Niveau n) throws ConditionAnalyseErrorException {
		boolean buf;
		int i=0;
		TypeCondition tc = table.get(i);
		if (tc.type == TypeDeCondition.cond) {
			buf = search(tc.getNum()).analyse(n);
		} else if (tc.type == TypeDeCondition.commut) {
			buf = (n.findSwitchById(tc.getNum())).getEtat();
		} else {
			throw new ConditionAnalyseErrorException("");
		}
		
		if (i+1 < table.size()) {
			i++;
			TypeCondition operator = table.get(i);
			if (i+1 < table.size()) {
				i++;
				tc = table.get(i);
				boolean part2 = true;
				if (tc.type == TypeDeCondition.cond) {
					part2 = search(tc.getNum()).analyse(n);
				} else if (tc.type == TypeDeCondition.commut) {
					part2 = (n.findSwitchById(tc.getNum())).getEtat();
				} else {
					throw new ConditionAnalyseErrorException("");
				}
				if(operator.type == TypeDeCondition.and) {
					buf = buf & part2;
				} else if (operator.type == TypeDeCondition.or) {
					buf = buf | part2;
				} else {
					throw new ConditionAnalyseErrorException("");
				}
			} else {
				throw new ConditionAnalyseErrorException("");
			}
		}
		
		for (int j=3; j<table.size(); j++) {
			TypeCondition operator = table.get(j);
			if (j+1 < table.size()) {
				j++;
				tc = table.get(j);
				boolean part = true;
				if (tc.type == TypeDeCondition.cond) {
					part = search(tc.getNum()).analyse(n);
				} else if (tc.type == TypeDeCondition.commut) {
					part = (n.findSwitchById(tc.getNum())).getEtat();
				} else {
					throw new ConditionAnalyseErrorException("");
				}
				if(operator.type == TypeDeCondition.and) {
					buf = buf & part;
				} else if (operator.type == TypeDeCondition.or) {
					buf = buf | part;
				} else {
					throw new ConditionAnalyseErrorException("");
				}
			} else {
				throw new ConditionAnalyseErrorException("");
			}
		}
		
		if (this.is == TypeCas.off) {
			return !buf;
		} else {
			return buf;
		}
	}
	
	@Override
	public String toString() {
		String str = "( ";
		for (TypeCondition t : this.table) {
			if (t.type == TypeDeCondition.commut) {
				str += "SWITCH "+t.getNum();
			}
			if (t.type == TypeDeCondition.cond) {
				Condition c = Condition.search(t.getNum());
				str += c.toString();
				if (c.getIs() == TypeCas.on) {
					str += "IS ON";
				} else if (c.getIs() == TypeCas.off) {
					str += "IS OFF";
				}
				
			}
			if (t.type == TypeDeCondition.and) {
				str += "ET";
			}
			if (t.type == TypeDeCondition.or) {
				str += "OU";
			}
			str += " ";
		}
		str += ") ";
		return str;
	}
	
	// Statics Methods
	public static Condition search(int id) {
		for (Condition c : conditionRepository) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}
	
	
	// Getters
	public int getId() {
		return id;
	}
	public TypeCas getIs() {
		return is;
	}
	public List<TypeCondition> getTable(){
		return table;
	}
}
