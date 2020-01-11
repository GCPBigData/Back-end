import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBrokerage } from 'app/shared/model/brokerage.model';

type EntityResponseType = HttpResponse<IBrokerage>;
type EntityArrayResponseType = HttpResponse<IBrokerage[]>;

@Injectable({ providedIn: 'root' })
export class BrokerageService {
    public resourceUrl = SERVER_API_URL + 'api/brokerages';

    constructor(private http: HttpClient) {}

    create(brokerage: IBrokerage): Observable<EntityResponseType> {
        return this.http.post<IBrokerage>(this.resourceUrl, brokerage, { observe: 'response' });
    }

    update(brokerage: IBrokerage): Observable<EntityResponseType> {
        return this.http.put<IBrokerage>(this.resourceUrl, brokerage, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBrokerage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBrokerage[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
