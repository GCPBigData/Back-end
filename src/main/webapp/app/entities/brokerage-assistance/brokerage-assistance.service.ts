import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';

type EntityResponseType = HttpResponse<IBrokerageAssistance>;
type EntityArrayResponseType = HttpResponse<IBrokerageAssistance[]>;

@Injectable({ providedIn: 'root' })
export class BrokerageAssistanceService {
    public resourceUrl = SERVER_API_URL + 'api/brokerage-assistances';

    constructor(private http: HttpClient) {}

    create(brokerageAssistance: IBrokerageAssistance): Observable<EntityResponseType> {
        return this.http.post<IBrokerageAssistance>(this.resourceUrl, brokerageAssistance, { observe: 'response' });
    }

    update(brokerageAssistance: IBrokerageAssistance): Observable<EntityResponseType> {
        return this.http.put<IBrokerageAssistance>(this.resourceUrl, brokerageAssistance, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBrokerageAssistance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBrokerageAssistance[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
