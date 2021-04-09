import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import generated_files.*;

import java.io.IOException;
import  static  org.antlr.v4.runtime.CharStreams.fromFileName;

public class Launch {
    public static void main (String[] args){
        try{
            String source = "file_program_tests/test1";
            CharStream ca= fromFileName(source);
            tiny_parserLexer lexer= new tiny_parserLexer(ca);
            CommonTokenStream token= new CommonTokenStream(lexer);
            tiny_parserParser parser= new tiny_parserParser(token);
            tiny_parserParser.ProgramContext tree= parser.program();

            SymbolesGeneration visitor= new SymbolesGeneration();
            visitor.visitProgram(tree);
            System.out.println(visitor.getSemanticErrors());
            System.out.println(visitor.getSemantic_table());
            SymbolesTable table_symboles = visitor.getSemantic_table();

            Routines routines= new Routines(table_symboles);
            routines.visitProgram(tree);

            QuadrupletsGeneration visitorQuad = new QuadrupletsGeneration();
            QuadrupletsGeneration.initComparators();
            visitorQuad.visitProgram(tree);
            Quadruplets quadruplets = visitorQuad.getQuadruplets();

            for(QuadElement quad : quadruplets){
                System.out.println(quad);
            }
            CodeObjetGeneration codeObjet = new CodeObjetGeneration(quadruplets, table_symboles);
            for(String instruction : codeObjet){
                System.out.println(instruction);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
