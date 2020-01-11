import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBrokerageProduct } from 'app/shared/model/brokerage-product.model';

type EntityResponseType = HttpResponse<IBrokerageProduct>;
type EntityArrayResponseType = HttpResponse<IBrokerageProduct[]>;

@Injectable({ providedIn: 'root' })
export class BrokerageProductService {
    public resourceUrl = SERVER_API_URL + 'api/brokerage-products';

    constructor(private http: HttpClient) {}

    create(brokerageProduct: IBrokerageProduct): Observable<EntityResponseType> {
        return this.http.post<IBrokerageProduct>(this.resourceUrl, brokerageProduct, { observe: 'response' });
    }

    update(brokerageProduct: IBrokerageProduct): Observable<EntityResponseType> {
        return this.http.put<IBrokerageProduct>(this.resourceUrl, brokerageProduct, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBrokerageProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBrokerageProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
