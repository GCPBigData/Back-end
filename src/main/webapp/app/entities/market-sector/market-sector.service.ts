import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMarketSector } from 'app/shared/model/market-sector.model';

type EntityResponseType = HttpResponse<IMarketSector>;
type EntityArrayResponseType = HttpResponse<IMarketSector[]>;

@Injectable({ providedIn: 'root' })
export class MarketSectorService {
    public resourceUrl = SERVER_API_URL + 'api/market-sectors';

    constructor(private http: HttpClient) {}

    create(marketSector: IMarketSector): Observable<EntityResponseType> {
        return this.http.post<IMarketSector>(this.resourceUrl, marketSector, { observe: 'response' });
    }

    update(marketSector: IMarketSector): Observable<EntityResponseType> {
        return this.http.put<IMarketSector>(this.resourceUrl, marketSector, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMarketSector>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMarketSector[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
