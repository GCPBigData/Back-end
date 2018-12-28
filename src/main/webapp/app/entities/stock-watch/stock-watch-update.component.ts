import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IStockWatch } from 'app/shared/model/stock-watch.model';
import { StockWatchService } from './stock-watch.service';
import { IStock } from 'app/shared/model/stock.model';
import { StockService } from 'app/entities/stock';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-stock-watch-update',
    templateUrl: './stock-watch-update.component.html'
})
export class StockWatchUpdateComponent implements OnInit {
    stockWatch: IStockWatch;
    isSaving: boolean;

    stocks: IStock[];

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private stockWatchService: StockWatchService,
        private stockService: StockService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ stockWatch }) => {
            this.stockWatch = stockWatch;
        });
        this.stockService.query().subscribe(
            (res: HttpResponse<IStock[]>) => {
                this.stocks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.stockWatch.id !== undefined) {
            this.subscribeToSaveResponse(this.stockWatchService.update(this.stockWatch));
        } else {
            this.subscribeToSaveResponse(this.stockWatchService.create(this.stockWatch));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStockWatch>>) {
        result.subscribe((res: HttpResponse<IStockWatch>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackStockById(index: number, item: IStock) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
