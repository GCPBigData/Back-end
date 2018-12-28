import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStockWatch } from 'app/shared/model/stock-watch.model';

type EntityResponseType = HttpResponse<IStockWatch>;
type EntityArrayResponseType = HttpResponse<IStockWatch[]>;

@Injectable({ providedIn: 'root' })
export class StockWatchService {
    public resourceUrl = SERVER_API_URL + 'api/stock-watches';

    constructor(private http: HttpClient) {}

    create(stockWatch: IStockWatch): Observable<EntityResponseType> {
        return this.http.post<IStockWatch>(this.resourceUrl, stockWatch, { observe: 'response' });
    }

    update(stockWatch: IStockWatch): Observable<EntityResponseType> {
        return this.http.put<IStockWatch>(this.resourceUrl, stockWatch, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IStockWatch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStockWatch[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
