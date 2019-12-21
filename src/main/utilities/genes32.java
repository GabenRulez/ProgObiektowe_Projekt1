package utilities;

import java.util.ArrayList;

public class genes32 {
    private int[] array;

    public genes32(){
        this.array = new int[]{1,1,1,1,1,1,1,1};
        for(int i=0; i<24; i++){
            int value = (int) Math.floor(8 * Math.random());
            this.array[value]++;
        }
    }

    private genes32(genes32 copy){
        this.array = new int[]{0,0,0,0,0,0,0,0};
        for(int i=0; i<8; i++){
            this.array[i] = copy.array[i];
        }
    }

    public genes32 getGenesBetween(int start, int end){         // resulting array includes both "start index" and "end index" element
        genes32 genesPart = new genes32( this );

        int startCol = 0;
        while( start != 0 ){
            if(this.array[startCol] <= start ){
                genesPart.array[startCol] = 0;
                start -= this.array[startCol];
                startCol++;
            }
            else{
                genesPart.array[startCol] -= start;
                start = 0;
            }
        }

        int endCol = 7;
        int reverseEnd = 31 - end;
        while( reverseEnd != 0 ){
            if(this.array[endCol] <= reverseEnd ){
                genesPart.array[endCol] = 0;
                reverseEnd -= this.array[endCol];
                endCol--;
            }
            else{
                genesPart.array[endCol] -= reverseEnd;
                reverseEnd = 0;
            }
        }

        return genesPart;
    }

    public genes32 addGenesParts(genes32 part1, genes32 part2, genes32 part3){
        genes32 finalGenes = new genes32(part1);

        for(int i=0; i<8; i++){
            finalGenes.array[i] += part2.array[i] + part3.array[i];
        }

        return finalGenes;
    }

    public int geneIndex(int index){
        int startCol = 0;
        while( index != 0 ){
            if(this.array[startCol] <= index ){
                index -= this.array[startCol];
                startCol++;
            }
            else return startCol;
        }
        return startCol;
    }

    public void repairGenes(){
        ArrayList<Integer> missingIndexes = new ArrayList<>();
        ArrayList<Integer> extraIndexes = new ArrayList<>();

        boolean needForRepair = false;
        for (int i=0; i<8; i++){
            if (this.array[i] < 1) {
                needForRepair = true;
                missingIndexes.add(i);
            }
        }
        if(!needForRepair) return;

        for(int i=0; i<8; i++){
            if(this.array[i]>1){
                for(int j=1; j<this.array[i]; j++){
                    extraIndexes.add(i);
                }
            }
        }
        // Here I have missingIndexes containg all indexes that are missing, and I can choose any value to insert them from extraIndexes list.

        for (Integer missingIndex : missingIndexes) {
            int valueToExchange = extraIndexes.remove((int) Math.floor(extraIndexes.size() * Math.random()));
            this.array[missingIndex] += 1;
            this.array[valueToExchange] -= 1;
        }
        // All indexes should have value of at least 1
    }
}
