/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas2;

import java.util.ArrayList;

/**
 *
 * @author MQGuilherme
 */
class Tabuleiro {
    private ArrayList <Peca> brancas = new ArrayList(12);
    private ArrayList <Peca> pretas = new ArrayList(12);
    private Celula[][] tabuleiro;
    private int val;

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
    public Tabuleiro(int tam){
        tabuleiro = new Celula[tam][tam];
        inicializarTabuleiro();
        criaPecas();
        distribuiPecas();
    }
    
    public Tabuleiro(){
        tabuleiro = new Celula[8][8];
        inicializarTabuleiro();
    }
    
    private void criaPecas(){
        for (int i=0;i<24;i++){
            if(i>=12){
                Peca pr = new Peca("pr"+((i+1)-12),"C");
                pretas.add(pr);
            }else{
                Peca br = new Peca("br"+(i+1),"H");
                brancas.add(br);
            }
        }
    }
    
    private void inicializarTabuleiro(){
        for(int i = 0; i < tabuleiro.length; i++){
            for(int j = 0; j < tabuleiro[0].length; j++){
                Celula cel = new Celula(i,j);
                //Pesos para heuristica posicional
                if(i == 0 || i == 7 && j >= 0 && j <= 7)
                    cel.setPeso(4);
                else if((j == 0 || j == 7) && i >= 0 && i <= 7)
                    cel.setPeso(4);
                if((i == 1 || i == 6) && j > 0 && j < 7)
                    cel.setPeso(3);
                else if((j == 1 || j == 6) && i > 0 && i < 7)
                    cel.setPeso(3);
                if((i == 2 || i == 5) && j >= 2 && j <= 5)
                    cel.setPeso(2);
                else if((j == 2 || j == 5) && i >= 2 && i <= 5)
                    cel.setPeso(2);
                if((i == 3 || i == 4) && (j == 3 || j == 4))
                    cel.setPeso(1);
                
                
                tabuleiro[i][j] = cel;
            }
            
        }
    }
    
    private void distribuiPecas() {
        int aux=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
               
                if (i%2==0) {
                    brancas.get(aux).setPosX(i);
                    brancas.get(aux).setPosY(j*2+1);
                    tabuleiro[i][j*2+1].setPeca(brancas.get(aux));                   
                }else
                {
                    brancas.get(aux).setPosX(i);
                    brancas.get(aux).setPosY(j*2);
                    tabuleiro[i][j*2].setPeca(brancas.get(aux));
                }
                
                if (i%2==0) {
                    pretas.get(aux).setPosX(i+5);
                    pretas.get(aux).setPosY(j*2);
                    tabuleiro[i+5][j*2].setPeca(pretas.get(aux));
                    
                }else
                {
                    pretas.get(aux).setPosX(i+5);
                    pretas.get(aux).setPosY(j*2+1);
                    tabuleiro[i+5][j*2+1].setPeca(pretas.get(aux));
                    
                }
                aux++;
            }
        }
    }
    
    @Override
    public String toString(){
        String resp = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j].isEmpty()) {
                    resp+=" ______ ";
                }else
                    resp+=" "+tabuleiro[i][j].getPeca().getId()+" "+ "(" + tabuleiro[i][j].getX() + "," + tabuleiro[i][j].getY() + ")";   
            }
            resp+="\n";
        }
        return resp;
    }
    
    
    public void printTabuleiro(){
        for (int i = 0; i < 8; i++) {
            if(i==0)
                System.out.print("       "+i+"    ");
            else
                System.out.print("   "+i+"    ");
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print(i+"  ");
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j].isEmpty()) {
                    System.out.print( "  ____  " );
                }else{
                    if(tabuleiro[i][j].getPeca().getId().length()==3)
                        System.out.print("   "+tabuleiro[i][j].getPeca().getId()+"  ");  
                    else
                        System.out.print("  "+tabuleiro[i][j].getPeca().getId()+"  ");  
                }
            }
            System.out.println("\n");
        }
    }
    
    public ArrayList<Peca> getBrancas() {
        return brancas;
    }

    public ArrayList<Peca> getPretas() {
        return pretas;
    }

    public Celula[][] getTabuleiro() {
        return tabuleiro;
    }

    public void setBrancas(ArrayList<Peca> brancas) {
        this.brancas = brancas;
    }

    public void setPretas(ArrayList<Peca> pretas) {
        this.pretas = pretas;
    }

    public void setTabuleiro(Celula[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    //Heuristica Posicional
    public int funcaoDeAvaliacao() {
        int valorBrancas = 0;
        int aux = 0;
        for (int i = 0; i < brancas.size(); i++) {
            Peca p = brancas.get(i);
            
            if (p.getPosX() == 6 && p.getJogador().equals("C") && !p.eh_Dama()) {
                aux = 7;
            } else {
                aux = 5;
            }
            if (p.getPosX() == 1 && p.getJogador().equals("H") && !p.eh_Dama()) {
                aux = 7;
            } else {
                aux = 5;
            }
            if (p.eh_Dama()) {
                aux = 10;
            }
            valorBrancas += aux * (this.tabuleiro[p.getPosX()][p.getPosY()].getPeso());
            aux = 0;
        }
        aux = 0;
        int valorPretas = 0;
        for (int i = 0; i < pretas.size(); i++) {
            Peca p = pretas.get(i);
            
            if (p.getPosX() == 6 && p.getJogador().equals("C") && !p.eh_Dama()) {
                aux = 7;
            } else {
                aux = 5;
            }
            if (p.getPosX() == 1 && p.getJogador().equals("H") && !p.eh_Dama()) {
                aux = 7;
            } else {
                aux = 5;
            }
            if (p.eh_Dama()) {
                aux = 10;
            }
            valorPretas += aux * (this.tabuleiro[p.getPosX()][p.getPosY()].getPeso());
            aux = 0;
        }
        
        
        return valorPretas - valorBrancas;
        
    }

    
    public ArrayList<Jogada> verificaJogadas(Peca p){
        if(!p.eh_Dama()){
            return filtraJogadas(avaliaJogadasPecaComum(p));
        }else{
            return filtraJogadas(avaliaJogadasDama(p));
        }
    }
    
    private void verificaCaptura(ArrayList<Jogada> jogadas, 
                                Celula frente, Celula frenteSeguinte, 
                                Celula tras, Celula trasSeguinte, Peca p){
        if(frente!=null && frente.getPeca()!=null && frenteSeguinte!=null){
            if(!frente.getPeca().getJogador().equals(p.getJogador())
           && frenteSeguinte.isEmpty()){
                Peca pecaCapturar = frente.getPeca();
                jogadas.add(new Jogada("captura",frenteSeguinte,pecaCapturar));
            }
        }
        if(tras!=null && tras.getPeca()!=null && trasSeguinte!=null){//no caso da dama, estas células são nulas para este método
            if(!tras.getPeca().getJogador().equals(p.getJogador())
               && trasSeguinte.isEmpty()){
                Peca pecaCapturar = tras.getPeca();
                jogadas.add(new Jogada("captura",trasSeguinte,pecaCapturar));
            }
        }
    }
    
    /*Método responsável por gerar uma lista de possíveis jogadas de movimentação
    ou captura, de uma peça comum.*/
    private ArrayList<Jogada> avaliaJogadasPecaComum(Peca p){
        ArrayList<Jogada> provaveisJogadas = new ArrayList<>();
        Celula diagonalEsqFrente;
        Celula diagonalEsqFrenteSeg;
        Celula diagonalDirFrente;
        Celula diagonalDirFrenteSeg;
        Celula diagonalEsqTras;
        Celula diagonalDirTras;
        Celula diagonalEsqTrasSeg;
        Celula diagonalDirTrasSeg;

        if (p.getId().contains("pr")) { //verifica se a peça da vez é preta ou branca
            try {diagonalDirFrente = tabuleiro[p.getPosX() - 1][p.getPosY() + 1];} catch (Exception e) {diagonalDirFrente = null;}
            try {diagonalDirFrenteSeg = tabuleiro[p.getPosX() - 2][p.getPosY() + 2];} catch (Exception e) {diagonalDirFrenteSeg = null;}
            try {diagonalEsqFrente = tabuleiro[p.getPosX() - 1][p.getPosY() - 1];} catch (Exception e) {diagonalEsqFrente = null;}
            try {diagonalEsqFrenteSeg = tabuleiro[p.getPosX() - 2][p.getPosY() - 2];} catch (Exception e) {diagonalEsqFrenteSeg = null;}
            try {diagonalDirTras = tabuleiro[p.getPosX() + 1][p.getPosY() + 1];} catch (Exception e) {diagonalDirTras = null;}
            try {diagonalEsqTras = tabuleiro[p.getPosX() + 1][p.getPosY() - 1];} catch (Exception e) {diagonalEsqTras = null;}
            try {diagonalDirTrasSeg = tabuleiro[p.getPosX() + 2][p.getPosY() + 2];} catch (Exception e) {diagonalDirTrasSeg = null;}
            try {diagonalEsqTrasSeg = tabuleiro[p.getPosX() + 2][p.getPosY() - 2];} catch (Exception e) {diagonalEsqTrasSeg = null;}
        } else {
            try {diagonalEsqFrente = tabuleiro[p.getPosX() + 1][p.getPosY() - 1];} catch (Exception e) {diagonalEsqFrente = null;}
            try {diagonalEsqFrenteSeg = tabuleiro[p.getPosX() + 2][p.getPosY() - 2];} catch (Exception e) {diagonalEsqFrenteSeg = null;}
            try {diagonalDirFrente = tabuleiro[p.getPosX() + 1][p.getPosY() + 1];} catch (Exception e) {diagonalDirFrente = null;}
            try {diagonalDirFrenteSeg = tabuleiro[p.getPosX() + 2][p.getPosY() + 2];} catch (Exception e) {diagonalDirFrenteSeg = null;}
            try {diagonalDirTras = tabuleiro[p.getPosX() - 1][p.getPosY() + 1];} catch (Exception e) {diagonalDirTras = null;}
            try {diagonalEsqTras = tabuleiro[p.getPosX() - 1][p.getPosY() - 1];} catch (Exception e) {diagonalEsqTras = null;}
            try {diagonalDirTrasSeg = tabuleiro[p.getPosX() - 2][p.getPosY() + 2];} catch (Exception e) {diagonalDirTrasSeg = null;}
            try {diagonalEsqTrasSeg = tabuleiro[p.getPosX() - 2][p.getPosY() - 2];} catch (Exception e) {diagonalEsqTrasSeg = null;}
        }

        if (p.getPosY() < 6 && p.getPosY() > 1) { //a peça não está próxima ou exatamente,
                                                  //nas laterais
            if (diagonalEsqFrente != null) {
                if (diagonalEsqFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalEsqFrente));
                } else {
                    verificaCaptura(provaveisJogadas, diagonalEsqFrente, diagonalEsqFrenteSeg,
                            diagonalEsqTras, diagonalEsqTrasSeg, p);
                }
            }

            if (diagonalDirFrente != null) {
                if (diagonalDirFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalDirFrente));
                } else {
                    verificaCaptura(provaveisJogadas, diagonalDirFrente, diagonalDirFrenteSeg,
                            diagonalDirTras, diagonalDirTrasSeg, p);
                }
            }

        } else if (p.getPosY() == 0) { //a peça está na lateral esquerda
            if (diagonalDirFrente != null) {
                if (diagonalDirFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalDirFrente));
                } else {
                    verificaCaptura(provaveisJogadas, diagonalDirFrente, diagonalDirFrenteSeg,
                            diagonalDirTras, diagonalDirTrasSeg, p);
                }
            }

        } else if (p.getPosY() == 7) { //a peça está na lateral direita
            if (diagonalEsqFrente != null) {
                if (diagonalEsqFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalEsqFrente));
                } else {
                    verificaCaptura(provaveisJogadas, diagonalEsqFrente, diagonalEsqFrenteSeg,
                            diagonalEsqTras, diagonalEsqTrasSeg, p);
                }
            }
            
        } else if (p.getPosY() == 1) { //a peça está próxima a lateral esquerda
            if (diagonalEsqFrente != null) {
                if (diagonalEsqFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalEsqFrente));
                }
            }

            if (diagonalDirFrente != null) {
                if (diagonalDirFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalDirFrente));
                } else {
                    verificaCaptura(provaveisJogadas, diagonalDirFrente, diagonalDirFrenteSeg,
                            diagonalDirTras, diagonalDirTrasSeg, p);
                }
            }

        } else if (p.getPosY() == 6) { //a peça está próxima a lateral direita
            if (diagonalEsqFrente != null) {
                if (diagonalEsqFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalEsqFrente));
                } else {
                    verificaCaptura(provaveisJogadas, diagonalEsqFrente, diagonalEsqFrenteSeg,
                            diagonalEsqTras, diagonalEsqTrasSeg, p);
                }
            }

            if (diagonalDirFrente != null) {
                if (diagonalDirFrente.isEmpty()) {
                    provaveisJogadas.add(new Jogada("movimentacao", diagonalDirFrente));
                }
            }
        }
        return provaveisJogadas;
    }
    
    /*Método responsável por gerar uma lista de possíveis jogadas de movimentação
    ou captura, de uma dama.*/
    private ArrayList<Jogada> avaliaJogadasDama(Peca p){
        ArrayList<Jogada> provaveisJogadas = new ArrayList<>();
        Celula aux = new Celula(p.getPosX(),p.getPosY());
        int tempPosX,tempPosY;
        for (int i = 1; i < 5; i++) { //controle para percorrimento das 4 direções
            //(1->diagonalEsqFrente, 2->diagonalDirFrente, 3->diagonalDirTras, 4->diagonalEsqTras)
            switch (i) {
                case 1:
                    while(true){
                        tempPosX = aux.getX()-1;
                        tempPosY = aux.getY()-1;
                        aux = new Celula(tempPosX,tempPosY);
                        if((aux.getX()>=0 && aux.getX()<=7) && (aux.getY()>=0 && aux.getY()<=7)){
                            if(aux.isEmpty())
                                provaveisJogadas.add(new Jogada("movimentacao",aux));
                            else{
                                Celula seguinte;
                                try{seguinte = tabuleiro[aux.getX()-1][aux.getY()-1];}catch(Exception e){seguinte = null;}
                                if(seguinte!=null)
                                    verificaCaptura(provaveisJogadas,aux,seguinte,null,null,p);
                                break;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    break;
                case 2:
                    while(true){
                        tempPosX = aux.getX()-1;
                        tempPosY = aux.getY()+1;
                        aux = new Celula(tempPosX,tempPosY);
                        if((aux.getX()>=0 && aux.getX()<=7) && (aux.getY()>=0 && aux.getY()<=7)){
                            if(aux.isEmpty())
                                provaveisJogadas.add(new Jogada("movimentacao",aux));
                            else{
                                Celula seguinte;
                                try{seguinte = tabuleiro[aux.getX()-1][aux.getY()+1];}catch(Exception e){seguinte = null;}
                                if(seguinte!=null)
                                    verificaCaptura(provaveisJogadas,aux,seguinte,null,null,p);
                                break;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    break;
                case 3:
                    while(true){
                        tempPosX = aux.getX()+1;
                        tempPosY = aux.getY()+1;
                        aux = new Celula(tempPosX,tempPosY);
                        if((aux.getX()>=0 && aux.getX()<=7) && (aux.getY()>=0 && aux.getY()<=7)){
                            if(aux.isEmpty())
                                provaveisJogadas.add(new Jogada("movimentacao",aux));
                            else{
                                Celula seguinte;
                                try{seguinte = tabuleiro[aux.getX()+1][aux.getY()+1];}catch(Exception e){seguinte = null;}
                                if(seguinte!=null)
                                    verificaCaptura(provaveisJogadas,aux,seguinte,null,null,p);
                                break;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    break;
                case 4:
                    while(true){
                        tempPosX = aux.getX()+1;
                        tempPosY = aux.getY()-1;
                        aux = new Celula(tempPosX,tempPosY);
                        if((aux.getX()>=0 && aux.getX()<=7) && (aux.getY()>=0 && aux.getY()<=7)){
                            if(aux.isEmpty())
                                provaveisJogadas.add(new Jogada("movimentacao",aux));
                            else{
                                Celula seguinte;
                                try{seguinte = tabuleiro[aux.getX()+1][aux.getY()-1];}catch(Exception e){seguinte = null;}
                                if(seguinte!=null)
                                    verificaCaptura(provaveisJogadas,aux,seguinte,null,null,p);
                                break;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    break;
            }
            
        }
        return provaveisJogadas;   
    }

    private ArrayList<Jogada> filtraJogadas(ArrayList<Jogada> provaveisJogadas) {
        if(provaveisJogadas.contains(new Jogada("captura",null))){
            for (int i = 0; i < provaveisJogadas.size(); i++) {
                if(provaveisJogadas.get(i).getTipo().equals("movimentacao"))
                    provaveisJogadas.remove(i);
            }
        }
        return provaveisJogadas;
    }
    
    public String fimDeJogo(){
        String resp = "Fim de Jogo.";
        if(brancas.isEmpty())
            return resp+" Vencedor: COMPUTADOR";
        else {
            if(pretas.isEmpty())
                return resp+" Vencedor: HUMANO";
            else{
                return "";
            }
        }
    }
    
    public Tabuleiro copiaTabuleiro(){
        Tabuleiro tab = new Tabuleiro();
        tab.brancas = new ArrayList<>();
        tab.pretas = new ArrayList<>();
        for(int i = 0; i < this.getBrancas().size(); i++){
            String id = this.getBrancas().get(i).getId();
            String jogador = this.getBrancas().get(i).getJogador();
            int posX = this.getBrancas().get(i).getPosX();
            int posY = this.getBrancas().get(i).getPosY();
            Peca p = new Peca(id,jogador);
            p.setPosX(posX);
            p.setPosY(posY);
            tab.brancas.add(p);
        }
        
        for(int i = 0; i < this.pretas.size(); i++){
            String id = this.getPretas().get(i).getId();
            String jogador = this.getPretas().get(i).getJogador();
            int posX = this.getPretas().get(i).getPosX();
            int posY = this.getPretas().get(i).getPosY();
            Peca p = new Peca(id,jogador);
            p.setPosX(posX);
            p.setPosY(posY);
            tab.pretas.add(p);
        }
        
        
        for(int i = 0; i < tab.tabuleiro.length; i++){
            for(int j = 0; j < tab.tabuleiro[0].length; j++){
                int x = this.tabuleiro[i][j].getPeso();
                tab.tabuleiro[i][j].setPeso(x);
            }
        }
        
        ArrayList<Peca> aux = new ArrayList<>();
        aux.addAll(tab.brancas);
        aux.addAll(tab.pretas);
        
        tab.inicializarTabuleiro();
        
        for(int i = 0; i < aux.size(); i++){
            int posX = aux.get(i).getPosX();
            int posY = aux.get(i).getPosY();
            tab.tabuleiro[posX][posY].setPeca(aux.get(i));
        }
        
     
        
        return tab;
    }
    
    public ArrayList<Tabuleiro> possiveisProximasJogadas(){
        ArrayList<Tabuleiro> possiveisJogadas = new ArrayList<>();
        for (int i = 0; i < getPretas().size(); i++) {
            Peca peca = getPretas().get(i);
            ArrayList<Jogada> possibilidades_da_peca = verificaJogadas(peca);
            for (int j = 0; j < possibilidades_da_peca.size(); j++) {
                Tabuleiro copia2 = copiaTabuleiro();               
                Peca aux = copia2.getPretas().get(i);
                aux.setPosX(peca.getPosX());
                aux.setPosY(peca.getPosY());
                possibilidades_da_peca.get(j).realizaJogada(copia2, aux);
                copia2.setVal(copia2.funcaoDeAvaliacao());
                possiveisJogadas.add(copia2);
            }
        }
        
        return possiveisJogadas;
    }
    
    public Peca getPecaBranca(String id){
        for (int i = 0; i < brancas.size(); i++) {
            Peca p = brancas.get(i);
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }
    
    public Peca getPecaPreta(String id){
        for (int i = 0; i < pretas.size(); i++) {
            Peca p = pretas.get(i);
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }
}    