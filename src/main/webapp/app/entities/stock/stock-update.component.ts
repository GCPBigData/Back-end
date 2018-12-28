import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IStock } from 'app/shared/model/stock.model';
import { StockService } from './stock.service';
import { IMarketSector } from 'app/shared/model/market-sector.model';
import { MarketSectorService } from 'app/entities/market-sector';

@Component({
    selector: 'jhi-stock-update',
    templateUrl: './stock-update.component.html'
})
export class StockUpdateComponent implements OnInit {
    stock: IStock;
    isSaving: boolean;

    marketsectors: IMarketSector[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private stockService: StockService,
        private marketSectorService: MarketSectorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ stock }) => {
            this.stock = stock;
        });
        this.marketSectorService.query().subscribe(
            (res: HttpResponse<IMarketSector[]>) => {
                this.marketsectors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.stock.id !== undefined) {
            this.subscribeToSaveResponse(this.stockService.update(this.stock));
        } else {
            this.subscribeToSaveResponse(this.stockService.create(this.stock));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>) {
        result.subscribe((res: HttpResponse<IStock>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMarketSectorById(index: number, item: IMarketSector) {
        return item.id;
    }
}
