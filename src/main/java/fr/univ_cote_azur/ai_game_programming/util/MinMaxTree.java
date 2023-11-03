package fr.univ_cote_azur.ai_game_programming.util;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.IA;

import java.util.List;

public class MinMaxTree {
    private Tree<IA.MinimaxResult> tree;

   public Tree<IA.MinimaxResult> findNode(int cell, Color color, Tree<IA.MinimaxResult> tree){
       int mid;
       IA.MinimaxResult node;
       boolean isNodeFound = false;
       List<Tree<IA.MinimaxResult>> children = tree.getChildren();
       int startIndex = 0;
       int endIndex = children.size() - 1;
       while (!isNodeFound && (startIndex <= endIndex)){
           mid = Math.floorDiv ((endIndex - startIndex), 2);
           node = children.get(mid).getData();
           if (node.cell() == cell && node.color() == color){
               return children.get(mid);
           } else if (node.cell() == cell) {
               if (color.name().compareTo(node.color().name()) > 0){
                   startIndex = mid + 1;
               }else{
                   endIndex = mid - 1;
               }
           } else if (cell > node.cell()) {
               startIndex = mid + 1;
           } else {
                endIndex = mid - 1;
           }
       }
       return null;
   }

}
