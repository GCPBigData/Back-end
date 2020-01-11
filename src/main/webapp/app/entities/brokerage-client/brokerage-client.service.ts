import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBrokerageClient } from 'app/shared/model/brokerage-client.model';

type EntityResponseType = HttpResponse<IBrokerageClient>;
type EntityArrayResponseType = HttpResponse<IBrokerageClient[]>;

@Injectable({ providedIn: 'root' })
export class BrokerageClientService {
    public resourceUrl = SERVER_API_URL + 'api/brokerage-clients';

    constructor(private http: HttpClient) {}

    create(brokerageClient: IBrokerageClient): Observable<EntityResponseType> {
        return this.http.post<IBrokerageClient>(this.resourceUrl, brokerageClient, { observe: 'response' });
    }

    update(brokerageClient: IBrokerageClient): Observable<EntityResponseType> {
        return this.http.put<IBrokerageClient>(this.resourceUrl, brokerageClient, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBrokerageClient>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBrokerageClient[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
