package utils;

import java.util.LinkedList;
import java.util.List;


/**
 * Helps a bit with the paging.
 * This class (as the name suggests) doesn't generate any html,
 * but just holds information about the paging.
 *
 * @author hansi
 */
public class PaginationInfo implements Cloneable{

    // what's the current page? (starting at 1)
    public int page;

    // what's the action url (e.g. Application.listSomething)
    public String action;

    // what params from the current request should be included in the links?
    // e.g. if you're paging over a search and your textfield is named "q",
    // then you should call: paginationInfo.addParams( "q" ).
    // this will take the current value from the request parameters and includ it
    // in the link, so it's point to /application/listSomething?q=the_value&page=3
    public LinkedList<String> params = new LinkedList<String>();

    // number of items per page, i think 15 is an awsome default
    public int perPage = 15;

    // number of result items
    public int numItems;


    public PaginationInfo(){
    }


    public PaginationInfo( String action, int page, int perPage, int numItems, String ... params ){
        action( action ).page( page ).perPage( perPage ).numItems( numItems ).addParams( params );
    }

    /**
     * Set the action (url, e.g. Application.doSomething)
     * Allows method chaining
     */
    public PaginationInfo action( String action ){
        this.action = action;
        return this;
    }

    /**
     * Set the current page
     * Allows method chaining
     */
    public PaginationInfo page( int page ){
        this.page = page;
        return this;
    }

    /**
     * Set the number of items visible per page
     * Allows method chaining
     */
    public PaginationInfo perPage( int perPage ){
        this.perPage = perPage;
        return this;
    }

    /**
     * Set total number of result items
     * Allows method chaining
     */
    public PaginationInfo numItems( int numItems ){
        this.numItems = numItems;
        return this;
    }

    /**
     * Add query parameters' names which should be included in links
     * Allows method chaining
     */
    public PaginationInfo addParams( String ... params ){
        for( String param : params ){
            this.params.add( param );
        }

        return this;
    }

    /**
     * Add query parameters' names which should be included in links
     * Allows method chaining
     */
    public PaginationInfo addParams( List<String> params ){
        this.params.addAll( params );
        return this;
    }



    /**
     * Validates and returns the current page.
     * This requires numItems and perPage to be set to function correctly.
     * @return
     */
    public int getPage(){
        return Math.max( 1, Math.min( page, getNumPages() ) );
    }


    /**
     * Calculates and returns the total number of pages.
     * This requires numItems and perPage to be set to function correctly.
     * @return
     */
    public int getNumPages(){
        return 1 + (int)Math.floor( ( numItems - 1 )/(float)perPage );
    }



    @Override
    protected PaginationInfo clone() throws CloneNotSupportedException {
        return new PaginationInfo( action, page, perPage, numItems ).addParams( params );
    }
}