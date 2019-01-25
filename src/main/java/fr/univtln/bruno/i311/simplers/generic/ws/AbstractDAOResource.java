package fr.univtln.bruno.i311.simplers.generic.ws;

import fr.univtln.bruno.i311.simplers.generic.business.DAOBean;
import fr.univtln.bruno.i311.simplers.generic.dao.IdentifiableEntity;
import fr.univtln.bruno.i311.simplers.generic.dao.Page;
import fr.univtln.bruno.i311.simplers.generic.dao.exception.DAOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bruno on 03/12/14.
 */

@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public abstract class AbstractDAOResource<T extends IdentifiableEntity> implements DAOResource<T> {
    private int maxpageSize = 10;
    private DAOBean<T> daoBean;

    protected AbstractDAOResource(DAOBean<T> daoBean) {
        this.daoBean = daoBean;
    }

    @Override
    @GET
    @Path("{id}")
    public T find(@PathParam("id") Long id) throws DAOException {
        return daoBean.find(id);
    }

    @Override
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) throws DAOException {
        daoBean.delete(id);
    }

    public long size() throws DAOException {
        return daoBean.getSize();
    }

    @Override
    @HEAD
    public Response metadata() throws DAOException {
        return Response.status(Response.Status.OK)
                .header("X-Total-Item-Count", size())
                .header("Access-Control-Expose-Headers",
                        "X-Total-Item-Count, X-Total-Page-Count")
                .build();
    }

    @Override
    @GET
    public Response findAll(@QueryParam("reverse") @DefaultValue("false") boolean reverse,
                            @QueryParam("pagenumber") @DefaultValue("0") int pageNumber,
                            @QueryParam("perpage") @DefaultValue("-1") int perPage,
                            @QueryParam("limit") @DefaultValue("-1") int limit,
                            @Context UriInfo uriInfo) throws DAOException {
        Response response;
        if (pageNumber == -1)
            response = listToResponse(daoBean.findAll(reverse, limit), uriInfo)
                    .build();
        else {
            if (perPage < 0) perPage = getMaxPageSize();
            Page result = daoBean.findAllByPage(reverse, pageNumber, perPage, limit);
            response = pageToResponse(result, uriInfo)
                    .build();
        }
        return response;
    }

    protected Response.ResponseBuilder listToResponse(List list, UriInfo uriInfo) throws DAOException {
        return Response.status(Response.Status.OK).entity(list)
                .header("X-Total-Item-Count", list.size())
                .header("Access-Control-Expose-Headers",
                        "X-Total-Item-Count, X-Total-Page-Count");
    }

    protected Response.ResponseBuilder pageToResponse(Page page, UriInfo uriInfo) throws DAOException {
        List<String> links = new ArrayList<>();
        if (page.PAGE_NUMBER > 0) {
            links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=0" + "&perpage=" + page.PAGE_SIZE + ">; rel=\"first\"");
            links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=" + (page.PAGE_NUMBER + 1) + "&perpage=" + page.PAGE_SIZE + ">; rel=\"previous\"");
        }
        if (page.PAGE_NUMBER < page.TOTAL_PAGES - 1) {
            links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=" + (page.PAGE_NUMBER + 1) + "&perpage=" + page.PAGE_SIZE + ">; rel=\"next\"");
            links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=" + (page.TOTAL_PAGES - 1) + "&perpage=" + page.PAGE_SIZE + ">; rel=\"last\"");
        }

        return Response.status(Response.Status.OK).entity(page.content)
                .header("Link", links.stream().collect(Collectors.joining(", ")))
                .header("X-Total-Item-Count", page.TOTAL_ITEMS)
                .header("X-Total-Page-Count", page.TOTAL_PAGES)
                .header("Access-Control-Expose-Headers", "X-Total-Item-Count, X-Total-Page-Count");
    }

    @Override
    @GET
    @Path("ids")
    public List<Long> getIds(@QueryParam("reverse") @DefaultValue("false") boolean reverse) throws DAOException {
        return daoBean.getIds(reverse);
    }

    @Override
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public int create(List<T> list) throws DAOException {
        return daoBean.create(list);
    }

    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public T update(T t) throws DAOException {
        return daoBean.update(t);
    }

    @Override
    @DELETE
    public int delete() throws DAOException {
        return daoBean.deleteAll();
    }

    @Override
    @GET
    @Path("maxpagesize")
    public int getMaxPageSize() {
        return maxpageSize;
    }

    @Override
    @PUT
    @Path("maxpagesize")
    @RolesAllowed("admin")
    public void setMaxPageSize(int pageSize) {
        this.maxpageSize = pageSize;
    }
}

