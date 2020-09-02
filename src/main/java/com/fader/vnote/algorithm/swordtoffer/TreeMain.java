package com.fader.vnote.algorithm.swordtoffer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FaderW
 */
public class TreeMain {

    private Map<Integer, Integer> indexMap = new HashMap<>();

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            indexMap.put(inorder[i], i);
        }

        return findRoot(preorder, 0, preorder.length-1, inorder, 0, inorder.length-1);

    }

    private TreeNode findRoot(int[] preorder, int preStart, int preEnd, int[] inOrder, int inStart, int inEnd) {
        if (preStart > preEnd) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preStart]);
        if (preStart < preEnd) {
            int rootIndex = indexMap.get(root.val);
            int leftNodes = rootIndex - inStart;
            int rightNodes = inEnd - rootIndex;
            root.left = findRoot(preorder, preStart+1, preStart+leftNodes, inOrder, inStart, rootIndex-1);
            root.right = findRoot(preorder, preEnd-rightNodes+1, preEnd, inOrder, rootIndex+1, inEnd);
        }

        return root;
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
