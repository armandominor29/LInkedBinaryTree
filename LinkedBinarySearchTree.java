import java.util.Comparator;

/**
 * Created by armandominor on 9/24/16.
 */

 /* LinkedBinarySearchTree implements the BinarySearchTreeADT interface
 * with links.
         *
         * @author Lewis and Chase
        * @version 4.0
        */
public class LinkedBinarySearchTree<T> extends LinkedBinaryTree<T>
        implements BinarySearchTreeADT<T>
{
    /**
     * Creates an empty binary search tree.
     */
    public LinkedBinarySearchTree()
    {
        super();
    }

    /**
     * Creates a binary search with the specified element as its root.
     *
     * @param element the element that will be the root of the new binary
     *        search tree
     */
    public LinkedBinarySearchTree(T element)
    {
        super(element);

        if (!(element instanceof Comparable))
            throw new NonComparableElementException("LinkedBinarySearchTree");
    }

    /**
     * Adds the specified object to the binary search tree in the
     * appropriate position according to its natural order.  Note that
     * equal elements are added to the right.
     *
     * @param element the element to be added to the binary search tree
     */
    @Override
    public void addElement(T element)
    {
        if (!(element instanceof Comparable))
            throw new NonComparableElementException("LinkedBinarySearchTree");

        Comparable<T> comparableElement = (Comparable<T>)element;

        if (isEmpty())
            root = new BinaryTreeNode<T>(element);
        else
        {
            if (comparableElement.compareTo(root.getElement()) < 0)
            {
                if (root.getLeft() == null)
                    this.getRootNode().setLeft(new BinaryTreeNode<T>(element));
                else
                    addElement(element, root.getLeft());
            }
            else
            {
                if (root.getRight() == null)
                    this.getRootNode().setRight(new BinaryTreeNode<T>(element));
                else
                    addElement(element, root.getRight());
            }
        }
        modCount++;
    }

    /**
     * Adds the specified object to the binary search tree in the
     * appropriate position according to its natural order.  Note that
     * equal elements are added to the right.
     *
     * @param element the element to be added to the binary search tree
     */
    private void addElement(T element, BinaryTreeNode<T> node)
    {
        Comparable<T> comparableElement = (Comparable<T>)element;

        if (comparableElement.compareTo(node.getElement()) < 0)
        {
            if (node.getLeft() == null)
                node.setLeft(new BinaryTreeNode<T>(element));
            else
                addElement(element, node.getLeft());
        }
        else
        {
            if (node.getRight() == null)
                node.setRight(new BinaryTreeNode<T>(element));
            else
                addElement(element, node.getRight());
        }
    }


    /**
     * Removes the first element that matches the specified target
     * element from the binary search tree and returns a reference to
     * it.  Throws a ElementNotFoundException if the specified target
     * element is not found in the binary search tree.
     *
     * @param targetElement the element being sought in the binary search tree
     * @throws ElementNotFoundException if the target element is not found
     */
    @Override
    public T removeElement(T targetElement)
            throws ElementNotFoundException
    {
        T result = null;

        if (isEmpty())
            throw new ElementNotFoundException("LinkedBinarySearchTree");
        else
        {
            BinaryTreeNode<T> parent = null;
            if (((Comparable<T>)targetElement).equals(root.element))
            {
                result =  root.element;
                BinaryTreeNode<T> temp = replacement(root);
                if (temp == null)
                    root = null;
                else
                {
                    root.element = temp.element;
                    root.setRight(temp.right);
                    root.setLeft(temp.left);
                }

                modCount--;
            }
            else
            {
                parent = root;
                if (((Comparable)targetElement).compareTo(root.element) < 0)
                    result = removeElement(targetElement, root.getLeft(), parent);
                else
                    result = removeElement(targetElement, root.getRight(), parent);
            }
        }

        return result;
    }

    /**
     * Removes the first element that matches the specified target
     * element from the binary search tree and returns a reference to
     * it.  Throws a ElementNotFoundException if the specified target
     * element is not found in the binary search tree.
     *
     * @param targetElement the element being sought in the binary search tree
     * @param node the node from which to search
     * @param parent the parent of the node from which to search
     * @throws ElementNotFoundException if the target element is not found
     */
    private T removeElement(T targetElement, BinaryTreeNode<T> node, BinaryTreeNode<T> parent)
            throws ElementNotFoundException
    {
        T result = null;

        if (node == null)
            throw new ElementNotFoundException("LinkedBinarySearchTree");
        else
        {
            if (((Comparable<T>)targetElement).equals(node.element))
            {
                result =  node.element;
                BinaryTreeNode<T> temp = replacement(node);
                if (parent.right == node)
                    parent.right = temp;
                else
                    parent.left = temp;

                modCount--;
            }
            else
            {
                parent = node;
                if (((Comparable)targetElement).compareTo(node.element) < 0)
                    result = removeElement(targetElement, node.getLeft(), parent);
                else
                    result = removeElement(targetElement, node.getRight(), parent);
            }
        }

        return result;
    }

    /**
     * Returns a reference to a node that will replace the one
     * specified for removal.  In the case where the removed node has
     * two children, the inorder successor is used as its replacement.
     *
     * @param node the node to be removed
     * @return a reference to the replacing node
     */
    private BinaryTreeNode<T> replacement(BinaryTreeNode<T> node)
    {
        BinaryTreeNode<T> result = null;

        if ((node.left == null) && (node.right == null))
            result = null;

        else if ((node.left != null) && (node.right == null))
            result = node.left;

        else if ((node.left == null) && (node.right != null))
            result = node.right;

        else
        {
            BinaryTreeNode<T> current = node.right;
            BinaryTreeNode<T> parent = node;

            while (current.left != null)
            {
                parent = current;
                current = current.left;
            }

            current.left = node.left;
            if (node.right != current)
            {
                parent.left = current.right;
                current.right = node.right;
            }

            result = current;
        }

        return result;
    }

    /**
     * Removes elements that match the specified target element from
     * the binary search tree. Throws a ElementNotFoundException if
     * the specified target element is not found in this tree.
     *
     * @param targetElement the element being sought in the binary search tree
     * @throws ElementNotFoundException if the target element is not found
     */
    @Override
    public void removeAllOccurrences(T targetElement)
            throws ElementNotFoundException
    {
        removeElement(targetElement);

        try
        {
            while (contains((T)targetElement))
                removeElement(targetElement);
        }

        catch (Exception ElementNotFoundException)
        {
        }
    }

    /**
     * Removes the node with the least value from the binary search
     * tree and returns a reference to its element.  Throws an
     * EmptyCollectionException if this tree is empty.
     *
     * @return a reference to the node with the least value
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T removeMin() throws EmptyCollectionException
    {
        T result = null;

        if (isEmpty())
            throw new EmptyCollectionException("LinkedBinarySearchTree");
        else
        {
            if (root.left == null)
            {
                result = root.element;
                root = root.right;
            }
            else
            {
                BinaryTreeNode<T> parent = root;
                BinaryTreeNode<T> current = root.left;
                while (current.left != null)
                {
                    parent = current;
                    current = current.left;
                }
                result =  current.element;
                parent.left = current.right;
            }

            modCount--;
        }

        return result;
    }

    /**
     * Removes the node with the highest value from the binary
     * search tree and returns a reference to its element.  Throws an
     * EmptyCollectionException if this tree is empty.
     *
     * @return a reference to the node with the highest value
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T removeMax() throws EmptyCollectionException
    {
        T result= null;

        if (isEmpty())
            throw new EmptyCollectionException("LinkedBinarySearchTree");
        else {
            if (root.right == null){
                result = root.element;
                root = root.left;
            }
            else{
                BinaryTreeNode<T> parent = root;
                BinaryTreeNode<T> current = root.right;

                while (current.right != null) {
                    parent = current;
                    current = current.right;
                }

                result = current.element;
                parent.right = current.left;
            }
            modCount--;
        }
        return result;
    }

    /**
     * Returns the element with the least value in the binary search
     * tree. It does not remove the node from the binary search tree.
     * Throws an EmptyCollectionException if this tree is empty.
     *
     * @return the element with the least value
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T findMin() throws EmptyCollectionException
    {
        T result;

        if (isEmpty())
            throw new EmptyCollectionException("binary tree");
        else {
            BinaryTreeNode<T> current = root;

            while (current.left != null)
                current = current.left;

            result = current.element;
        } //else

        return result;
    }

    /**
     * Returns the element with the highest value in the binary
     * search tree.  It does not remove the node from the binary
     * search tree.  Throws an EmptyCollectionException if this
     * tree is empty.
     *
     * @return the element with the highest value
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T findMax() throws EmptyCollectionException
    {
        T result = null;

        if (isEmpty())
            throw new EmptyCollectionException("binary tree");
        else {
            BinaryTreeNode<T> current = root;

            while (current.right != null)
                current = current.right;

            result = current.element;
        } //else

        return result;
    }

/**
 * Returns a reference to the specified target element if it is
 * found in the binary tree.  Throws a NoSuchElementException if
 * the specified target element is not found in this tree.
 *
 * @param targetElement the element being sough in the binary tree
 * @throws ElementNotFoundException if the target element is not found
 */
//@Override
    public T find (T targetElement) throws ElementNotFoundException
    {
        BinaryTreeNode<T> current = root;

        if (!(current.element.equals(targetElement)) && (current.left != null) && (((Comparable) current.element).compareTo(targetElement) > 0))
            current = findNode(targetElement, current.left);

        else if (!(current.element.equals(targetElement)) && (current.right != null))
            current = findNode(targetElement, current.right);

        if (!(current.element.equals(targetElement)))
            throw new ElementNotFoundException("LinkedBinarySearchTree");

        return current.element;

    }

    /**
     * Returns a reference to the specified target element if it is
     * found in this tree.
     *
     * @param targetElement the element being sought in the tree
     * @param next the tree node to begin searching on
     */
    private BinaryTreeNode<T> findNode(T targetElement, BinaryTreeNode<T> next)
    {
        BinaryTreeNode<T> current = next;
        if (!(next.element.equals(targetElement)) && (next.left != null) && (((Comparable) next.element).compareTo(targetElement) > 0))
            next = findNode(targetElement, next.left);
        else if (!(next.element.equals(targetElement)) && (next.right != null))
            next = findNode(targetElement, next.right);

        return next;
    }

    public static void main( String [ ] args ) {
        LinkedBinarySearchTree<String> NFLTeams = new LinkedBinarySearchTree<String>();

        NFLTeams.addElement("Buccaneers");
        NFLTeams.addElement("Steelers");
        NFLTeams.addElement("Patriots");
        NFLTeams.addElement("Broncos");
        NFLTeams.addElement("Dolphins");
        System.out.println("Contains Broncos:");
        System.out.println(NFLTeams.contains("Broncos"));
        System.out.println("Find Patriots:");
        System.out.println(NFLTeams.find("Patriots"));
        System.out.println("Contains Buccaneers:");
        System.out.println(NFLTeams.contains("Buccaneers"));
        System.out.println("Find Min:");
        System.out.println(NFLTeams.findMin());
        System.out.println("Get Height:");
        System.out.println(NFLTeams.getHeight());
        System.out.println("Find Max:");
        System.out.println(NFLTeams.findMax());
        System.out.println("Print Root with left and right child:");
        System.out.println("\t"+NFLTeams.getRootElement());
        System.out.println(NFLTeams.getLeft().getRootElement()+"\t"+"\t"+NFLTeams.getRight().getRootElement());


    }
}