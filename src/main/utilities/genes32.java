package utilities;

public class genes32 {
    public int[] array;

    public genes32(){
        this.array = new int[]{4,4,4,4,4,4,4,4};
    }

    public genes32( genes32 copy ){
        System.arraycopy(copy.array, 0, this.array, 0, 8);
    }


    /*
    public genes32 copy(){

        return new int[]{this.array[0], this.array[1], this.array[2], this.array[3], this.array[4], this.array[5], this.array[6], this.array[7]};
    }

     */

    public genes32 getGenesBetween(int start, int end){
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





    public int[] toArray(){
        int[] result = new int[32];
        int arrayIterator = 0;
        for(int i=0; i<32; i++){
            while( array[arrayIterator] == 0 ) arrayIterator++;
            result[i] = arrayIterator;
        }
        return result;
    }
}
