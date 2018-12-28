import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IMarketSector } from 'app/shared/model/market-sector.model';
import { MarketSectorService } from './market-sector.service';

@Component({
    selector: 'jhi-market-sector-update',
    templateUrl: './market-sector-update.component.html'
})
export class MarketSectorUpdateComponent implements OnInit {
    marketSector: IMarketSector;
    isSaving: boolean;

    constructor(private marketSectorService: MarketSectorService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ marketSector }) => {
            this.marketSector = marketSector;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.marketSector.id !== undefined) {
            this.subscribeToSaveResponse(this.marketSectorService.update(this.marketSector));
        } else {
            this.subscribeToSaveResponse(this.marketSectorService.create(this.marketSector));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMarketSector>>) {
        result.subscribe((res: HttpResponse<IMarketSector>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
