import java.util.*;
 
class TokenLex {
 
    String Token = "";
    String Lex = "";
	String Type = "";
	String Classe = "";
	String Size = "";
    String Value = "";
	String Address = "";
 
    public TokenLex(){
        // contructor vazio
        this.Token = "";
        this.Lex = "";
		this.Type = "";
		this.Classe = "";
		this.Size = "";
		this.Value = "";
		this.Address = "";
    }
 
    public TokenLex(String token, String lex,String type,String classe, String size, String value, String address) {
        this.Token = token;
        this.Lex = lex;
		this.Type = type;
		this.Classe = classe;
		this.Size = size;
		this.Value = value;
		this.Address = address;
    }
 
    //Getter & Setters
    public String getToken(){
        return this.Token;
    }
 
    public String getLex(){
        return this.Lex;
    }
	
	public String getType(){
        return this.Lex;
    }
	
	public String getClasse(){
        return this.Lex;
    }
	
	public String getSize(){
        return this.Lex;
    }
	
	public String getValue(){
        return this.Lex;
    }
	
	public String getAddress(){
        return this.Lex;
    }
 
    public void setToken(String token){
        this.Token = token;
    }
   
    public void setLex(String lex){
        this.Lex = lex;
    }
	
	public void setType(String type){
		this.Type = type;
	}
	
	public void setClasse(String classe){
        this.Classe = classe;
    }
   
    public void setSize(String size){
        this.Size = size;
    }
	
	public void setValue(String value){
		this.Value = value;
	}
	
	public void setAddress(String address){
		this.Address = address;
	}
   
}
 
public class Compiler {
   
    // Objects
    private static TokenLex TL = new TokenLex();
    private static Scanner sc = new Scanner(System.in);
    private static TokenLex tempTok = new TokenLex();
    private static TokenLex tempTok2 = new TokenLex();
 
    // Global variables  
    private static String lexema = readFile();
    // used to get positions on the symbol table
    private static int index = 0;
    // used to travel navigate on the string
    private static int interator = 0;
    // global token string
    private static String token;
    public static boolean error = false;
	
	//flags
	public static boolean sinal = false;
 
    // Conferir os simbolos abaixo com nossa tabela de simbolos
    private static char[] letter = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','Y','V','W','X','Y','Z'};
    private static char[] digit = {'0','1','2','3','4','5','6','7','8','9'};                                                                                                                     
    private static char[] hex   = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};                                                                                             
    private static char[] symbols = { '=','(',')',',','+','-','*',';','{','}','[',']','%',':','&','^','@','!','?','>','<','\''};
    private static char[] quebra = {'D', 'A'};

    // Creation of symbol table
    private static Map<Integer,TokenLex> alphabet = new HashMap<Integer,TokenLex>();                                                                           
    private static String[] reservedWords = {"var","const", "integer", "char", "for", "if", "else", "and", "or", "not", "to","then", "readln", "step", "write", "writeln", "do", "(", ")", "<", ">", "<>", ">=", "=<",",", "+", "-", "*", "/", ";", "%", "[", "]", "="};
   
    public static String readFile() {
        String result = "";
        //Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            result = result + sc.nextLine().toLowerCase()+" ";
        }
        // System.out.println(result);
        return result;
    }
   
    public static boolean contains(char [] arr, char c){
        boolean result = false;
        for(int i=0; i<arr.length; i++){
            if(arr[i] == c){
                result = true;
            }
        }
        return result;
    }
 
    public static String returnIt(String l, int i){
        return lexema.substring(i, l.length());
    }
 
    public static boolean containsTok(String s, Map<Integer,TokenLex> m) {
        boolean result = false;
        for (Map.Entry<Integer, TokenLex> entry : m.entrySet()){
            if(s.equals(entry.getValue().getToken())){
                result = true;
            }
        }
        return result;
    }
	
	public static boolean isNumeric(String str) {  
		try{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}  
		return true;  
    }
	
	public static TokenLex searchToken(String s, Map<Integer,TokenLex> m){
        TokenLex result = new TokenLex();
        for (Map.Entry<Integer, TokenLex> entry : m.entrySet()){
            if(s.equals(entry.getValue().getLex())){
                result = entry.getValue();
            }
        }
        return result;
    }
 
    public static String getToken(String s, Map<Integer,TokenLex> m){
        String result = "";
        for (Map.Entry<Integer, TokenLex> entry : m.entrySet()){
            if(s.equals(entry.getValue().getToken())){
                result = entry.getValue().getToken();
            }
        }
        return result;
    }
 
    // TODO base on each state of the automat
    public static TokenLex analisadorLexico(){
       
        TokenLex tl = new TokenLex();
        String tmpLexema = "";
        int state = 0;
       
        for (;interator < lexema.length();) {
            char c = lexema.charAt(interator);
			//System.out.println("Symbol ->" + c);
            switch (state) {
                // Create every case equals of the automato que vc criou fazendo os if e etc pra cada um e separando  (sendo cada bolinha um case)
                case 0:
                    if (c == '_' || c == '.' || contains(letter, c)) {
                        interator++;  
                        tmpLexema = tmpLexema+c;
                        state = 2;
                        tl.setToken("id");
                    } else if ( c == '0') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // pode entrar em conflito com digito
                        interator++;
                        tmpLexema = tmpLexema+c;
						//System.out.println("state 10");
                        state = 10;
                        tl.setToken("const");
                    } else if ( contains(digit, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                        tl.setToken("const");
                    } else if ( c == '<') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 4;
                    } else if ( c == '>') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 5;
                    } else if ( c == '/') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        // duvida pro be
                        tmpLexema = tmpLexema+c;
                        state = 7;
                    } else if ( contains(symbols, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 99;
                        tl.setToken(tmpLexema);
                    } else if ( c == ' ') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
						//System.out.println("entrou no blank");
                        interator++;
                        state = 0;
                    } else if(c == '\"'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 13;
                        tl.setToken("const");
                    } else if (c == '\''){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 6;
                        tl.setToken("const");
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // Error
                        state = 666;
                    }
                    break;
                case 1:
                    if (contains(letter, c) || contains(digit, c) || c == '.' || c == '_'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    } else {
                       //System.out.println(" >> " + c + " << " + " -- " + state);
                        //System.out.println("--------------------" + tmpLexema +"---------------");
                        // novo lexama e formado
                        lexema = returnIt(lexema, interator);
						//System.out.println("");
                        // reseta posição
                        interator = 0;
                        // aceitação
                        state = 99;
                    }
                    break;
                case 2:
                    if(c == '.' || c == '_'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 2;
                    }else if(contains(letter, c) || contains(digit, c)){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 1;
                    }else{
						lexema = returnIt(lexema,interator);
						interator = 0;
						state = 99;
						
					}
                    break;
                case 3:
                    if(contains(digit, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = tmpLexema+c;
                        state = 3;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 4:
                    if( c == '='){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    }else if( c == '>') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;    
                    }
                    break;
                case 5:
                    if(c == '=') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        //aceitação
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 6:
                    if (contains(symbols, c) || contains(letter, c) || contains(digit, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 11;
                    }
                    break;
                case 7:
                // comentario ou dividir
                    if (c == '*') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        // criar dividir
                        lexema = returnIt(lexema, interator);
                        interator = 0;
                        state = 99;
                    }
                    break;
                case 8:
                    // comentario
                    if (c == '*') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 9;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    }
                    break;
                case 9:
                    // comentario finish
                    if(c == '/'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        tmpLexema = "";
                        state = 0;
                    } else {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        interator++;
                        state = 8;
                    }
                    break;
                case 10:
                    if (c == 'x'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
						//System.out.println("Quebra2");
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 12;
                    }else if(contains(quebra, c)) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
						//System.out.println("Quebra2");
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 14;
                    } else {
						lexema = returnIt(lexema, interator);
						interator = 0;
                        state = 99;
					}
                    break;
                case 11:
                    // Finishing char
                    if(c == '\''){
                       interator++;
                       state = 99;
                    }
                    break;
                case 12:
                    if (contains(hex, c)){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 15;
                    }
                    break;
                case 15:
                    if (contains(hex, c)){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
                    }
                    break;
                case 13:
                    if (contains(letter, c) || contains(digit, c) || contains(symbols, c) || c == ' ') {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 13;
                    } else if (c == '\"' ) {
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema + c ;
                        interator++;
                        state = 99;
                    }
                    break;
                case 14:
                    if (c == 'h'){
                        //System.out.println(" >> " + c + " << " + " -- " + state);
                        tmpLexema = tmpLexema+c;
                        interator++;
                        state = 99;
						System.out.println("leu quebra");
                    }
                    break;
                case 99:
                    if(!(containsTok(tmpLexema,alphabet))){
                        //if(!alphabet.containsKey(tmpLexema)){
                       
                        tl.setLex((tmpLexema));
                        alphabet.put(index,tl);
                        //System.out.println(" >> " + tl.getLex() + " << " + " -- " + tl.getToken());
                        index++;
                    } else {
                        //System.out.println(" >> " + tmpLexema + " << " + " -- " + state);
                        tl.setToken(getToken(tmpLexema, alphabet));
                    }
                    return tl;
                case 666:
                    //System.out.println(" >> " + c + " << " + " -- " + state);
                    interator = lexema.length();
                    System.out.println("Caractere inválido " + "( " + c + " )" );
                    break;
            }
        }
        if(state != 0 && state != 99 && state != 666 ){
            System.out.println("Fim de arquivo não esperado");
            error = true;
        }
        return tl;
    }
   
    //TODO
    // Lembrar de usar a variavel error aqui para parar o programa caso fim de arquivo não esperado
    public static void casaToken(String token_expected) {
        // toke esperado avalia e pega proximo
        //System.out.println(TL.getLex() + "----lex------");
        //System.out.println(TL.getToken() + "-----token----");
        if(TL.getToken().equals(token_expected)) {
            TL = analisadorLexico();
            //System.out.println("TL:"+TL.getLex() + "----lex------"+TL.getToken() + "-----token----");
			//System.out.println("lalalala");
            if(error == true){
                //System.out.println("anem");
                System.exit(0);
            }
        }else{
            System.out.println("ERRO: Token nao esperado ("+TL.getToken()+")");
            System.exit(0);
        }
 
    }
 
    public static void S (){
        while(TL.getToken().equals("var") || TL.getToken().equals("const") || TL.getToken().equals("char")){
            D();
        }while(interator < lexema.length()){
        //while(TL.getToken().equals("id") || TL.getToken().equals("For") || TL.getToken().equals("if") || TL.getToken().equals(";") || TL.getToken().equals("readln") || TL.getToken().equals("write") || TL.getToken().equals("writeln")){
            C();
        }
 
    }
 
    public static void D(){
        if(TL.getToken().equals("var")){
			//System.out.println(" Entrou D");
            	casaToken("var");
                casaToken("integer");
                tempTok = searchToken(TL.getLex(), alphabet);
                if(tempTok.getClasse().equals("")){
                    tempTok.setClasse("var");
                    tempTok.setType("integer");
                    //System.out.println("casatoken integer");
                    casaToken("id");
                Y();
                // acao semantica 18
                }else{
                    System.out.println("Error : Identificador ja declarado: [" + tempTok.getLex() + "]");
                }
				//System.out.println("Saiu Y");
                while(TL.getToken().equals(",")){
                    casaToken(",");
                    tempTok = searchToken(TL.getLex(), alphabet);
                    if(tempTok.getClasse().equals("")){
                        tempTok.setClasse("var");
                        tempTok.setType("integer");
                        casaToken("id");
                        Y();
                        // acao semantica 18
                    }else{
                    System.out.println("Error: identificador ja declarado: ["+tempTok.getLex()+"]" );
                    }
                }
				//System.out.println("Antes do ;");
                casaToken(";");
				//System.out.println("passou pelo ;");
        }
        else if (TL.getToken().equals("char")){
				//System.out.println("antes do char");
                casaToken("char");
                //System.out.println("casou char");
                tempTok = searchToken(TL.getLex(), alphabet);
                if(tempTok.getClasse().equals("")){
                    tempTok.setClasse("var");
                    tempTok.setType("char");
                    casaToken("id");
                    Y();
                    // acao semantica 18
                }else{
                    System.out.println("Error: identificador ja declarado: ["+tempTok.getLex()+"]" );   
                }
                while(TL.getToken().equals(",")){
                    casaToken(",");
                    tempTok = searchToken(TL.getLex(), alphabet);
                    if(tempTok.getClasse().equals("")){
                        tempTok.setClasse("var");
                        tempTok.setType("char");
                        casaToken("id");
                        Y();
                        // acao semantica 18
                    }else{
                        System.out.println("Error: identificador ja declarado: ["+tempTok.getLex()+"]" );   
                    }
                }
                casaToken(";");  
        }else{
            casaToken("const");
            tempTok = searchToken(TL.getLex(), alphabet);
            if(tempTok.getClasse().equals("")){
                tempTok.setClasse("const");
                tempTok.setType("int");
                casaToken("id");
                casaToken("=");
                if(TL.getToken().equals(" - ")){
                    casaToken("-");
                }
                TL.setClasse("const");
                casaToken("const");
            }else{
                System.out.println("Error: identificador ja declarado: ["+tempTok.getLex()+"]" );
            }

        }
    }
	
	//descomentar caso toda declaração de variavel tenha que ter Var na frente
	/*
	    public static void D(){
        if(TL.getToken().equals("var")){
			//System.out.println(" Entrou D");
            casaToken("var");
            if(TL.getToken().equals("integer")){
                casaToken("integer");
				//System.out.println("casatoken integer");
                casaToken("id");
                Y();
				//System.out.println("Saiu Y");
                while(TL.getToken().equals(",")){
                    casaToken(",");
                    casaToken("id");
                    Y();
                }
				//System.out.println("Antes do ;");
                casaToken(";");
				//System.out.println("passou pelo ;");
            }else{
				System.out.println("antes do char");
                casaToken("char");
				System.out.println("casou char");
                casaToken("id");
                Y();
                while(TL.getToken().equals(",")){
                    casaToken(",");
                    casaToken("id");
                    Y();
                }
                casaToken(";");  
            }
        }
        else{
          casaToken("const");
          casaToken("id");
          if(TL.getToken().equals(" - ")){
              casaToken("-");
          }
          casaToken("const");  
        }
    }*/
 
    public static void Y(){
        if(TL.getToken().equals("=")){
            casaToken("=");
            if(TL.getToken().equals("-")){
                casaToken("-");
                sinal = true;
            }
        }
        sinal = false;
        if(isNumeric(TL.getLex())){
            TL.setType("int");
        }else{
            TL.setType("char");
        }
        tempTok2 = searchToken(TL.getLex(), alphabet);
        if(tempTok.getType().equals(tempTok2.getType())){
            tempTok.setValue(tempTok2.getLex());
        }
        casaToken("const");
        if(TL.getToken().equals("[")){
        casaToken("[");
        tempTok.setSize(TL.getLex());
        if(isNumeric(TL.getLex())){
            if(0 < Integer.parseInt(TL.getLex()) && Integer.parseInt(TL.getLex()) < 2048){
                tempTok.setSize(TL.getLex());
            }else{
                System.out.println("Error: tamanho do vetor excede o máximo permitido");
            }
        }else{
            System.out.println("Error: Tipos incompativeis.");
        }
        casaToken("const");
        casaToken("]");
        }
    }
 
    public static void C(){
        if(TL.getToken().equals("id")){
            casaToken("id");
            casaToken("=");
            Exp();
            casaToken(";");
        }
        else if(TL.getToken().equals("for")){
            casaToken("for");
            casaToken("id");
            casaToken("=");
            Exp();
            casaToken("to");
            Exp();
            if(TL.getToken().equals("step")){
                casaToken("step");
                casaToken("const");
            }
            casaToken("do");
            if(TL.getToken().equals("{")){
                casaToken("{");
                while(!(TL.getToken().equals("}"))){
                    C();
                }
                casaToken("}");    
            }else{
                C();
            }
        }
        else if (TL.getToken().equals("if")){
            casaToken("if");
            Exp();
            casaToken("then");
            W();
            casaToken("else");
            W();
        }
        else if (TL.getToken().equals(";")){
            casaToken(";");
        }
        else if (TL.getToken().equals("readln")){
            casaToken("readln");
            casaToken("(");
            casaToken("id");
            if(TL.getToken().equals("[")){
                casaToken("[");
                Exp();
                casaToken("]");
            }
            casaToken(")");
            casaToken(";");
        }
        else if (TL.getToken().equals("write")){
            casaToken("write");
            casaToken("(");
            Exp();
            while(TL.getToken().equals(",")){
                casaToken(",");
                Exp();
            }
            casaToken(")");
            casaToken(";");
        }
        else if (TL.getToken().equals("writeln")){
            casaToken("writeln");
            casaToken("(");
            Exp();
            while(TL.getToken().equals(",")){
                casaToken(",");
                Exp();
            }
            casaToken(")");
            casaToken(";");
        }
    }
 
    public static void W(){
        if(TL.getToken().equals("{")){
            casaToken("{");
            C();
            while(!(TL.getToken().equals("}"))){
                C();
            }
            casaToken("}");
        }
        else{
           C();
        }
    }
 
    public static void Exp(){
        //System.out.println("Exp");
        ExpS();
        while(TL.getToken().equals("==") || TL.getToken().equals("<>") || TL.getToken().equals("<") || TL.getToken().equals(">") || TL.getToken().equals("<=") || TL.getToken().equals(">=")){
            if(TL.getToken().equals("==")){
                casaToken("==");
            }
            else if(TL.getToken().equals("<>")){
                casaToken("<>");
            }
            else if(TL.getToken().equals("<")){
                casaToken("<");
            }
            else if(TL.getToken().equals(">")){
                casaToken(">");
            }
            else if(TL.getToken().equals("<=")){
                casaToken("<=");
            }
            else if(TL.getToken().equals(">=")){
                casaToken(">=");
            }
           
            ExpS();
        }
    }
    public static void ExpS(){
        if(TL.getToken().equals("+")){
            casaToken("+");
        }
        else if (TL.getToken().equals("-")){
            casaToken("-");
        }
        else{
           // System.out.println("ExpS");
            T();
            while(TL.getToken().equals("+") || TL.getToken().equals("-") || TL.getToken().equals("or")){
                if(TL.getToken().equals("+")){
                    casaToken("+");
                }
                else if(TL.getToken().equals("-")){
                    casaToken("-");
                }
                else if(TL.getToken().equals("or")){
                    casaToken("or");
                }
               
                T();   
            }
        }
    }
    public static void T(){
        //System.out.println("T");
        F();
        while(TL.getToken().equals("*") || TL.getToken().equals("/") || TL.getToken().equals("and") || TL.getToken().equals("%")){
            if(TL.getToken().equals("*")){
                casaToken("*");
            }else if (TL.getToken().equals("/")){
                casaToken("/");
            }else if (TL.getToken().equals("and")){
                casaToken("and");
            }else if (TL.getToken().equals("%")){
                casaToken("%");
            }
           
            F();
           
        }
    }
 
    public static void F(){
       // System.out.println("F");
        if(TL.getToken().equals("(")){
            casaToken("(");
            Exp();
            casaToken(")");
        }else if(TL.getToken().equals("not")){
            casaToken("not");
            F();
        }else if(TL.getToken().equals("const")){
            casaToken("const");
        }else if(TL.getToken().equals("id")){
            casaToken("id");
           if(TL.getToken().equals("[")){
                casaToken("[");
                Exp();
                casaToken("]");
            }
        }
    }
    // Program Principal -- chama tudo ai dentro
    public static void main (String [] args) {
 
        for(String word: reservedWords){
            TokenLex tl = new TokenLex(word, word, "", "", "", "", "");
            alphabet.put(index, tl);
            index++;
        }
        /*int x = 0;
        while (x < 200){
            token = analisadorLexico();
            x++;
        }*/
 
        TL = analisadorLexico();
        S();
        /*for(Map.Entry<Integer, TokenLex> entry : alphabet.entrySet()){   //print keys and values
            System.out.println(entry.getKey() + " : " +entry.getValue().getToken()+", "+entry.getValue().getLex());
        }*/
        if(interator < lexema.length()){
            System.out.println("Fim de arquivo não esperado!");
        }
       
    }
}
